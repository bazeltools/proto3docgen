_SHELL_SCRIPT_TEMPLATE = """
    set -e

    mkdir -p tmp_json_out

    {protoc_path} {imports} --plugin=protoc-gen-doc={plugin_path} \
        --doc_out=tmp_json_out --doc_opt=json,out.json {direct_sources}
    mv tmp_json_out/out.json {doc_out}
\n"""


def _print_aspect_impl(target, ctx):
    # if ProtoInfo in target:
    #     print(target[ProtoInfo])

    ret = depset()
    if ProtoInfo in target:
        doc_json = ctx.actions.declare_file("%s-protoc-json-doc.json" % ctx.rule.attr.name)
        script_file = ctx.actions.declare_file("script-%s.sh" % ctx.rule.attr.name)

        transitive_sources = depset(transitive = [target[ProtoInfo].transitive_sources])

        transitive_roots = depset(transitive = [target[ProtoInfo].transitive_proto_path])
        transitive_source_paths = " ".join(["-I" + d for d in transitive_roots.to_list()])
        direct_sources = depset(transitive = [f.files for f in ctx.rule.attr.srcs])
        direct_paths = " ".join([d.path for d in target[ProtoInfo].direct_sources])

        script_contents = _SHELL_SCRIPT_TEMPLATE.format(
            protoc_path = ctx.executable._generator.path,
            imports = transitive_source_paths,
            plugin_path = ctx.attr._plugin.files.to_list()[0].path,
            doc_out = doc_json.path,
            direct_sources = direct_paths,
        )
        ctx.actions.write(script_file, script_contents, True)
        ctx.actions.run(
            executable = script_file.path,
            arguments = [],
            inputs = depset(
                [ctx.executable._generator],
                transitive = [
                    direct_sources,
                    transitive_sources,
                    ctx.attr._plugin.files,
                ]
            ),
            outputs = [doc_json],
            tools = [script_file],
            mnemonic = "ProtoDocJsonBuild",
        )
        ret = depset([doc_json])

    return struct(output_groups = {
            "protodoc_output_json": ret,
        })

print_aspect = aspect(
    implementation = _print_aspect_impl,
    attr_aspects = ['deps'],
    required_aspect_providers = [[ProtoInfo]],
    attrs = {
        "_generator": attr.label(
            default = Label("@com_google_protobuf//:protoc"),
            executable = True,
            cfg = "host",
        ),
        "_plugin": attr.label(
            # todo
            default = Label("//proto-plugin:protoc-gen-doc-darwin"),
            allow_files = True,
            cfg = "host",
        ),
    }
)
