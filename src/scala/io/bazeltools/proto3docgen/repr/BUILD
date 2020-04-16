scala_library(
    name = "repr",
srcs = glob(["*.scala"]),
    deps = [
        "//src/scala/io/bazeltools/proto3docgen/md",
        "@third_party_jvm//3rdparty/jvm/io/circe:circe_core",
        "@third_party_jvm//3rdparty/jvm/io/circe:circe_parser",
        "@third_party_jvm//3rdparty/jvm/io/circe:circe_derivation",
        "//src/scala/io/bazeltools/proto3docgen/context",
    ],
    visibility = [
        "//visibility:public"
    ],
    exports = [
        "//src/scala/io/bazeltools/proto3docgen/md",
    ]
)
