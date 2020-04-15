set -e

PROTO3_DOC_GEN_VERSION=$1

cat >run_doc_generation.sh <<EOL
#!/bin/bash

set -e
SCRIPTS_DIR="\$( cd "\$( dirname "\${BASH_SOURCE[0]}" )" && pwd )"
ORIGINAL_PWD="\$(pwd)"


TMPDIR="\${TMPDIR:-/tmp}"
RND_UID=\$(date "+%s")
WORKING_PATH="\${TMPDIR}/\${RND_UID}"
mkdir -p \$WORKING_PATH
WORKING_PATH="\$(cd \$WORKING_PATH && pwd)"
cleanup() {
 rm -rf \$WORKING_PATH
}
ASPECT_REPO_PATH="\$WORKING_PATH/aspect_repo"
mkdir -p "\$ASPECT_REPO_PATH"

if [ -z "\$BAZEL" ]; then
  BAZEL=bazel
fi

if [ -z "\$GITHUB_BASE_URL" ]; then
  GITHUB_BASE_URL="https://github.com"
fi


cd \$ASPECT_REPO_PATH
curl -L -o proto3repo.zip \${GITHUB_BASE_URL}/bazeltools/proto3docgen/releases/download/${PROTO3_DOC_GEN_VERSION}/proto3repo.zip
unzip proto3repo.zip
rm proto3repo.zip

cd \$ORIGINAL_PWD

PROTO_BIN_PATH="\$WORKING_PATH/bep.protobin"
JSON_FILE_LIST="\$WORKING_PATH/output_json_file_list"

set +e
\$BAZEL build --build_event_binary_file "\$PROTO_BIN_PATH" \
  --override_repository=protoc_doc_helper="\$ASPECT_REPO_PATH" \
  --aspects @protoc_doc_helper//:aspect.bzl%print_aspect \
  --output_groups=protodoc_output_json \
  --keep_going \
  //...
set -e

if [ ! -f "\$PROTO_BIN_PATH" ]; then
  echo "Output proto wasn't produced, unknown failure"
  exit 1
fi


\$BAZEL run --override_repository=protoc_doc_helper="\$ASPECT_REPO_PATH" @protoc_doc_helper//:ExtractOutputGroups -- "\$PROTO_BIN_PATH" "\$JSON_FILE_LIST"

\$BAZEL run --override_repository=protoc_doc_helper="\$ASPECT_REPO_PATH" @protoc_doc_helper//:MarkdownGen -- -j "\$JSON_FILE_LIST" "\$@"
EOL

chmod +x run_doc_generation.sh
