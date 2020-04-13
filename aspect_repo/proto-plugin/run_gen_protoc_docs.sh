set -e

# Let us specify JAVA_FORMATTER_PATH on the command line, otherwise grab it via the first arg or
# some default value.
ORIGINAL_PWD=$PWD

echo "1"
DIRECT_PATH=$1
TRANSITIVE_PATHS=$2
OUTPUT_PATH=$3

echo $DIRECT_PATH
echo $TRANSITIVE_PATHS
echo $OUTPUT_PATH

echo "hello world" > $OUTPUT_PATH
exit 0
echo "2"
echo "3"
rm -rf doc
mkdir doc

echo "4"
$PROTOC_PATH -Ibazel-gptn/external/com_google_protobuf/src -I. --plugin=protoc-gen-doc=$PLUGIN_PATH --doc_out=./doc --doc_opt=json,output.json $(find src/main/proto -name '*.proto')

echo "5"
exit 0
