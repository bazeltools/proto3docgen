exports_files(["protoc-gen-doc-darwin", "protoc-gen-doc-linux"])

config_setting(
    name = "osx_plat",
    constraint_values = [
        "@platforms//os:osx",
    ],
)
config_setting(
    name = "linux_plat",
    constraint_values = [
        "@platforms//os:linux",
    ],
)

alias(name = "proto-gen-doc",
     actual = select(
     {

          ":osx_plat": ":protoc-gen-doc-darwin",
          ":linux_plat": ":protoc-gen-doc-linux",
      }
  ))

