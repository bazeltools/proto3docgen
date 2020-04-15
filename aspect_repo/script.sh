set -e

SCRIPTS_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
target_repo=`pwd`

set +e
bazel build \
  --override_repository=protoc_doc_helper="$SCRIPTS_DIR" \
  --aspects @protoc_doc_helper//:aspect.bzl%print_aspect \
  --output_groups=protodoc_output_json \
  --keep_going \
  //...
set -e

GENERATED_JSON=/tmp/protoc-doc-inputs
OUTPUT_PATH_ROOT=$(bazel info output_path 2> /dev/null)
find "${OUTPUT_PATH_ROOT}/darwin-fastbuild/bin/src/main/proto" -name "*-protoc-json-doc.json" > "$GENERATED_JSON"
cat $GENERATED_JSON

bazel run \
  --override_repository=protoc_doc_helper=/Users/siddarth/workspace/proto3docgen \
  @protoc_doc_helper//src/scala/io.bazeltools/proto3docgen:genmarkdown
