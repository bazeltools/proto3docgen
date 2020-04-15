set -e
SCRIPTS_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

OUTPUT_PATH=$1
if [ -z "$OUTPUT_PATH" ]; then
  echo "Please specify an output path"
  exit 1
fi

# if its not absolute stick the pwd in there.
case $OUTPUT_PATH in
  /*) ;;
  *) OUTPUT_PATH=$PWD/$OUTPUT_PATH ;;
esac

#Clean out any double slashes
OUTPUT_PATH="$(cd $(dirname $OUTPUT_PATH) && pwd)/$(basename $OUTPUT_PATH)"

cd $SCRIPTS_DIR/..
REPO_ROOT=$(pwd)



TMPDIR="${TMPDIR:-/tmp}"
RND_UID=$(date "+%s")



WORKING_PATH="${TMPDIR}/${RND_UID}"

mkdir -p $WORKING_PATH
WORKING_PATH="$(cd $WORKING_PATH && pwd)"

cleanup() {
 rm -rf $WORKING_PATH
}
trap cleanup EXIT

echo $WORKING_PATH

rsync -aP $REPO_ROOT/aspect_repo/ $WORKING_PATH/


for PLATFORM in "darwin" "linux"; do
  cd $WORKING_PATH
  rm -rf tmp_dl
  mkdir tmp_dl
  cd tmp_dl
  URL=https://github.com/pseudomuto/protoc-gen-doc/releases/download/v1.3.1/protoc-gen-doc-1.3.1.${PLATFORM}-amd64.go1.12.6.tar.gz
  curl -o output.tar.gz -L $URL
  tar -zxvf output.tar.gz
  mv protoc-gen-doc-1.3.1.${PLATFORM}-amd64.go1.12.6/protoc-gen-doc $WORKING_PATH/proto-plugin/protoc-gen-doc-${PLATFORM}
done
rm -rf tmp_dl


cd $REPO_ROOT
./bazel build src/scala/io/bazeltools/proto3docgen:GenerateMarkdown_deploy.jar

mv bazel-bin/src/scala/io/bazeltools/proto3docgen/GenerateMarkdown_deploy.jar $WORKING_PATH/

./bazel build src/scala/io/bazeltools/proto3docgen/extract_output_groups:ExtractOutputGroups_deploy.jar
mv bazel-bin/src/scala/io/bazeltools/proto3docgen/extract_output_groups/ExtractOutputGroups_deploy.jar $WORKING_PATH/

cd $WORKING_PATH
zip -r $OUTPUT_PATH .
