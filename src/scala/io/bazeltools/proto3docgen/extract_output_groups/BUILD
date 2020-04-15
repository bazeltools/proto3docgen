scala_library(
    name = "extract_output_groups",
    srcs = glob(["*.scala"]),
    deps = [
        "@com_google_protobuf//:protobuf_java",
        "@io_bazel//src/main/java/com/google/devtools/build/lib/buildeventstream/proto:build_event_stream_java_proto",
    ],
)

java_binary(
    name = "ExtractOutputGroups",
    main_class = "io.bazeltools.proto3docgen.extract_output_groups.ExtractOutputGroups",
    runtime_deps = [":extract_output_groups"],
)
