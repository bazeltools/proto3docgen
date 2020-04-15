workspace(name = "io_bazeltools_proto3docgen")

BAZEL_VERSION = "2.1.1"

BAZEL_INSTALLER_VERSION_linux_SHA = "d6cea18d59e9c90c7ec417b2645834f968132de16d0022c7439b1e60438eb8c9"

BAZEL_INSTALLER_VERSION_darwin_SHA = "b4c94148f52854b89cff5de38a9eeeb4b0bcb3fb3a027330c46c468d9ea0898b"

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
load(
    "@bazel_tools//tools/build_defs/repo:git.bzl",
    "git_repository",
    "new_git_repository",
)

git_repository(
    name = "io_bazel_rules_scala",
    commit = "a676633dc14d8239569affb2acafbef255df3480",  # HEAD as of 2020-01-15, update this as needed
    remote = "https://github.com/bazelbuild/rules_scala",
)

http_archive(
    name = "com_google_protobuf",
    sha256 = "1e622ce4b84b88b6d2cdf1db38d1a634fe2392d74f0b7b74ff98f3a51838ee53",
    strip_prefix = "protobuf-3.8.0",
    urls = ["https://github.com/protocolbuffers/protobuf/archive/v3.8.0.zip"],
)

http_archive(
    name = "bazel_skylib",
    sha256 = "7832382668c6dde9f57e18923763a24f9087cac66a50fbcc5afca848d03f2aa1",
    strip_prefix = "bazel-skylib-b113ed5d05ccddee3093bb157b9b02ab963c1c32",
    urls = ["https://github.com/bazelbuild/bazel-skylib/archive/b113ed5d05ccddee3093bb157b9b02ab963c1c32.tar.gz"],
)

load("@com_google_protobuf//:protobuf_deps.bzl", "protobuf_deps")

protobuf_deps()

http_archive(
    name = "io_bazel",
    patch_cmds = [
        """find ./ -type f -name BUILD | xargs sed -i.bak -e 's/visibility = \\[[^]]*\\]/visibility = \\["\\/\\/visibility:public"\\]/g'""",
    ],
    sha256 = "d9bf0a4d3457027ce4398969a1d69b9aa99e51d1d6e7d9291c31ba871f9e965c",
    strip_prefix = "bazel-3.0.0",
    url = "https://github.com/bazelbuild/bazel/archive/3.0.0.tar.gz",
)

load("@io_bazel_rules_scala//scala:scala.bzl", "scala_repositories")

scala_repositories((
    "2.12.10",
    {
        "scala_compiler": "cedc3b9c39d215a9a3ffc0cc75a1d784b51e9edc7f13051a1b4ad5ae22cfbc0c",
        "scala_library": "0a57044d10895f8d3dd66ad4286891f607169d948845ac51e17b4c1cf0ab569d",
        "scala_reflect": "56b609e1bab9144fb51525bfa01ccd72028154fc40a58685a1e9adcbe7835730",
    },
))

register_toolchains("//:scala_toolchain")

load("//3rdparty:workspace.bzl", "maven_dependencies")

maven_dependencies()

load("//3rdparty:target_file.bzl", "build_external_workspace")

build_external_workspace(name = "third_party_jvm")

bind(
    name = "io_bazel_rules_scala/dependency/scalatest/scalatest",
    actual = "//3rdparty/jvm/org/scalatest",
)
