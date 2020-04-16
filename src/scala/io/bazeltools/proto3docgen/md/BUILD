scala_library(
    name = "md",
    srcs = [
        "Heading.scala",
        "Section.scala",
        "Sectionable.scala",
        "Table.scala",
    ],
    deps = [
        "@third_party_jvm//3rdparty/jvm/io/circe:circe_core",
        "@third_party_jvm//3rdparty/jvm/io/circe:circe_parser",
        "@third_party_jvm//3rdparty/jvm/io/circe:circe_derivation",
        "@third_party_jvm//3rdparty/jvm/org/typelevel:paiges_core",
        "//src/scala/io/bazeltools/proto3docgen/context",
    ],
    exports = [
        "@third_party_jvm//3rdparty/jvm/org/typelevel:paiges_core",
    ],
    visibility = [
        "//visibility:public",
    ],
)
