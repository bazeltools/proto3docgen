scala_library(
    name = "genmarkdown",
    srcs = glob(["*.scala"]),
    deps = [
      "@third_party_jvm//3rdparty/jvm/io/circe:circe_core",
      "@third_party_jvm//3rdparty/jvm/io/circe:circe_parser",
      "@third_party_jvm//3rdparty/jvm/io/circe:circe_derivation",
      "@third_party_jvm//3rdparty/jvm/com/github/scopt",
      "@third_party_jvm//3rdparty/jvm/org/typelevel:paiges_core",
      "//src/scala/io/bazeltools/proto3docgen/repr",
      "//src/scala/io/bazeltools/proto3docgen/context",
    ],
    visibility = ["//visibility:public"]
)


java_binary(
    name = "GenerateMarkdown",
    runtime_deps = [
      ":genmarkdown",
    ],
    main_class = "io.bazeltools.proto3docgen.GenerateMarkdown",
    visibility = ["//visibility:public"]
)
