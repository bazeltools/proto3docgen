scala_library(
    name = "repr",
    srcs = [
        "Enum.scala",
        "Extension.scala",
        "File.scala",
        "ProtodocJson.scala",
        "ProtoPackage.scala",
        "Message.scala",
        "Service.scala",
    ],
    deps = [
        ":context",
        "//src/scala/io/bazeltools/proto3docgen/md",
        "@third_party_jvm//3rdparty/jvm/io/circe:circe_core",
        "@third_party_jvm//3rdparty/jvm/io/circe:circe_parser",
        "@third_party_jvm//3rdparty/jvm/io/circe:circe_derivation",
    ],
    visibility = [
        "//visibility:public"
    ],
    exports = [
        "//src/scala/io/bazeltools/proto3docgen/md",
        ":context",
    ]
)

scala_library(
    name = "context",
    srcs = [
        "Context.scala",
    ],
    deps = [],
    visibility = [
        "//visibility:public"
    ],
)
