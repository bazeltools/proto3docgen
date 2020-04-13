# Do not edit. bazel-deps autogenerates this file from dependencies.yaml.
def _jar_artifact_impl(ctx):
    jar_name = "%s.jar" % ctx.name
    ctx.download(
        output=ctx.path("jar/%s" % jar_name),
        url=ctx.attr.urls,
        sha256=ctx.attr.sha256,
        executable=False
    )
    src_name="%s-sources.jar" % ctx.name
    srcjar_attr=""
    has_sources = len(ctx.attr.src_urls) != 0
    if has_sources:
        ctx.download(
            output=ctx.path("jar/%s" % src_name),
            url=ctx.attr.src_urls,
            sha256=ctx.attr.src_sha256,
            executable=False
        )
        srcjar_attr ='\n    srcjar = ":%s",' % src_name

    build_file_contents = """
package(default_visibility = ['//visibility:public'])
java_import(
    name = 'jar',
    tags = ['maven_coordinates={artifact}'],
    jars = ['{jar_name}'],{srcjar_attr}
)
filegroup(
    name = 'file',
    srcs = [
        '{jar_name}',
        '{src_name}'
    ],
    visibility = ['//visibility:public']
)\n""".format(artifact = ctx.attr.artifact, jar_name = jar_name, src_name = src_name, srcjar_attr = srcjar_attr)
    ctx.file(ctx.path("jar/BUILD"), build_file_contents, False)
    return None

jar_artifact = repository_rule(
    attrs = {
        "artifact": attr.string(mandatory = True),
        "sha256": attr.string(mandatory = True),
        "urls": attr.string_list(mandatory = True),
        "src_sha256": attr.string(mandatory = False, default=""),
        "src_urls": attr.string_list(mandatory = False, default=[]),
    },
    implementation = _jar_artifact_impl
)

def jar_artifact_callback(hash):
    src_urls = []
    src_sha256 = ""
    source=hash.get("source", None)
    if source != None:
        src_urls = [source["url"]]
        src_sha256 = source["sha256"]
    jar_artifact(
        artifact = hash["artifact"],
        name = hash["name"],
        urls = [hash["url"]],
        sha256 = hash["sha256"],
        src_urls = src_urls,
        src_sha256 = src_sha256
    )
    native.bind(name = hash["bind"], actual = hash["actual"])


def list_dependencies():
    return [
    {"artifact": "com.github.scopt:scopt_2.12:4.0.0-RC2", "lang": "scala", "sha1": "a03ef9692c0333742baaa1a3babdbd2a6aeb97c4", "sha256": "d19a4e8b8c013a56e03bc57bdf87abe6297c974cf907585d00284eae61c6ac91", "repository": "https://repo.maven.apache.org/maven2/", "url": "https://repo.maven.apache.org/maven2/com/github/scopt/scopt_2.12/4.0.0-RC2/scopt_2.12-4.0.0-RC2.jar", "source": {"sha1": "cfad099db1ae22d37d29e54c0349f3fbaf1eae16", "sha256": "5727eaf75dbce75c1061434c0db343c7af2db32bcb45190963cdd1c3339a86cd", "repository": "https://repo.maven.apache.org/maven2/", "url": "https://repo.maven.apache.org/maven2/com/github/scopt/scopt_2.12/4.0.0-RC2/scopt_2.12-4.0.0-RC2-sources.jar"} , "name": "com_github_scopt_scopt_2_12", "actual": "@com_github_scopt_scopt_2_12//jar:file", "bind": "jar/com/github/scopt/scopt_2_12"},
# duplicates in io.circe:circe-core_2.12 fixed to 0.12.3
# - io.circe:circe-derivation_2.12:0.12.0-M7 wanted version 0.12.1
# - io.circe:circe-jawn_2.12:0.12.3 wanted version 0.12.3
# - io.circe:circe-parser_2.12:0.12.3 wanted version 0.12.3
    {"artifact": "io.circe:circe-core_2.12:0.12.3", "lang": "scala", "sha1": "30adafb5e4b68aac7d326107e00c8c4149e9806e", "sha256": "76d4f0f17612d3c35edf49d61ce00ba92348700985caa525787d21ff819be4e3", "repository": "https://repo.maven.apache.org/maven2/", "url": "https://repo.maven.apache.org/maven2/io/circe/circe-core_2.12/0.12.3/circe-core_2.12-0.12.3.jar", "source": {"sha1": "a6c8de14c40c1ba1f9cc7adc8e629194e2232508", "sha256": "509b3f559dc8cc6c8078ab38cdd76aa8d0bf5169bb0a23d5548636fa63c7929a", "repository": "https://repo.maven.apache.org/maven2/", "url": "https://repo.maven.apache.org/maven2/io/circe/circe-core_2.12/0.12.3/circe-core_2.12-0.12.3-sources.jar"} , "name": "io_circe_circe_core_2_12", "actual": "@io_circe_circe_core_2_12//jar:file", "bind": "jar/io/circe/circe_core_2_12"},
    {"artifact": "io.circe:circe-derivation_2.12:0.12.0-M7", "lang": "scala", "sha1": "8792f6ae203b6a5a70a1e990a90450993245c43a", "sha256": "0c66cddf1dac560f61a4bdc869d6114ab250a3842a259ddee1ee643873cf205b", "repository": "https://repo.maven.apache.org/maven2/", "url": "https://repo.maven.apache.org/maven2/io/circe/circe-derivation_2.12/0.12.0-M7/circe-derivation_2.12-0.12.0-M7.jar", "source": {"sha1": "9c80cd022f591da1e12891f24c9a5347294ec9fd", "sha256": "aafe2778581da8478172cafecc03239043f5330f56d3938bc591c22448503ffa", "repository": "https://repo.maven.apache.org/maven2/", "url": "https://repo.maven.apache.org/maven2/io/circe/circe-derivation_2.12/0.12.0-M7/circe-derivation_2.12-0.12.0-M7-sources.jar"} , "name": "io_circe_circe_derivation_2_12", "actual": "@io_circe_circe_derivation_2_12//jar:file", "bind": "jar/io/circe/circe_derivation_2_12"},
    {"artifact": "io.circe:circe-jawn_2.12:0.12.3", "lang": "scala", "sha1": "8cfd44053f9aba6f3c35f462fdc33fbc6c883f11", "sha256": "230426508fc55f5dfc63ccf29b05719a545d73d5ae79c96812e54b748877ff77", "repository": "https://repo.maven.apache.org/maven2/", "url": "https://repo.maven.apache.org/maven2/io/circe/circe-jawn_2.12/0.12.3/circe-jawn_2.12-0.12.3.jar", "source": {"sha1": "4b8214409d530124092b3a6d798b0357d3ba341f", "sha256": "df1c689c8e1cc8a2369ae4fc030a4e559603ec871bcc3d854a99a30eb093408e", "repository": "https://repo.maven.apache.org/maven2/", "url": "https://repo.maven.apache.org/maven2/io/circe/circe-jawn_2.12/0.12.3/circe-jawn_2.12-0.12.3-sources.jar"} , "name": "io_circe_circe_jawn_2_12", "actual": "@io_circe_circe_jawn_2_12//jar:file", "bind": "jar/io/circe/circe_jawn_2_12"},
    {"artifact": "io.circe:circe-numbers_2.12:0.12.3", "lang": "scala", "sha1": "49983243e497138378dc24642e461013d6a545d6", "sha256": "1623d8235150de91054461d8b5390dd32ab8dc133f3005ed06179599423d6576", "repository": "https://repo.maven.apache.org/maven2/", "url": "https://repo.maven.apache.org/maven2/io/circe/circe-numbers_2.12/0.12.3/circe-numbers_2.12-0.12.3.jar", "source": {"sha1": "b14407190ba24de9bc1f9077a5a6844799d0a4ac", "sha256": "11e59be9c076c02ba3fb1b439abfbdda6cb5871e749fddfe3437b94f72afe0ef", "repository": "https://repo.maven.apache.org/maven2/", "url": "https://repo.maven.apache.org/maven2/io/circe/circe-numbers_2.12/0.12.3/circe-numbers_2.12-0.12.3-sources.jar"} , "name": "io_circe_circe_numbers_2_12", "actual": "@io_circe_circe_numbers_2_12//jar:file", "bind": "jar/io/circe/circe_numbers_2_12"},
    {"artifact": "io.circe:circe-parser_2.12:0.12.3", "lang": "scala", "sha1": "1ca756f9fcc261e89823a85ea4476b8d414c85ce", "sha256": "c6b25097bb54710df9411835798ca40ce8f52e14a4c2630b930a119045bbc930", "repository": "https://repo.maven.apache.org/maven2/", "url": "https://repo.maven.apache.org/maven2/io/circe/circe-parser_2.12/0.12.3/circe-parser_2.12-0.12.3.jar", "source": {"sha1": "96f8d44bcafe4b45a12baf813bd4d31387ab0b90", "sha256": "ec6065753332e81790806839d3dd8d22a61fdc08ea5edef741011e4ec179a32d", "repository": "https://repo.maven.apache.org/maven2/", "url": "https://repo.maven.apache.org/maven2/io/circe/circe-parser_2.12/0.12.3/circe-parser_2.12-0.12.3-sources.jar"} , "name": "io_circe_circe_parser_2_12", "actual": "@io_circe_circe_parser_2_12//jar:file", "bind": "jar/io/circe/circe_parser_2_12"},
    {"artifact": "org.typelevel:cats-core_2.12:2.0.0", "lang": "scala", "sha1": "b15de4ed2b0f31b118acab7d12cab4962df2130c", "sha256": "65d828985463e6f14761a6451b419044b9f06507f292ac7ebc04133912b01339", "repository": "https://repo.maven.apache.org/maven2/", "url": "https://repo.maven.apache.org/maven2/org/typelevel/cats-core_2.12/2.0.0/cats-core_2.12-2.0.0.jar", "source": {"sha1": "b5f015d7c5908c4b68d01927f38a49e1906060e7", "sha256": "e6c7b838d4734acba23df9bf3e633300cbe998bcf724c8fcb2a5f1e866585505", "repository": "https://repo.maven.apache.org/maven2/", "url": "https://repo.maven.apache.org/maven2/org/typelevel/cats-core_2.12/2.0.0/cats-core_2.12-2.0.0-sources.jar"} , "name": "org_typelevel_cats_core_2_12", "actual": "@org_typelevel_cats_core_2_12//jar:file", "bind": "jar/org/typelevel/cats_core_2_12"},
    {"artifact": "org.typelevel:cats-kernel_2.12:2.0.0", "lang": "scala", "sha1": "c570a566ca5ed9def6f73adc2308d9d3260f49d5", "sha256": "e9d8fa3381b3d8e66261437227f9c926a5a4109c4448f6c4bb98f9575e9c38c5", "repository": "https://repo.maven.apache.org/maven2/", "url": "https://repo.maven.apache.org/maven2/org/typelevel/cats-kernel_2.12/2.0.0/cats-kernel_2.12-2.0.0.jar", "source": {"sha1": "f9c9abdbad4849b79a80c01ac653493e776a1b57", "sha256": "7ba77aea45ba5685cf9ada6437ef56fe726af09ccd8f3f48ecb842479d1c2eee", "repository": "https://repo.maven.apache.org/maven2/", "url": "https://repo.maven.apache.org/maven2/org/typelevel/cats-kernel_2.12/2.0.0/cats-kernel_2.12-2.0.0-sources.jar"} , "name": "org_typelevel_cats_kernel_2_12", "actual": "@org_typelevel_cats_kernel_2_12//jar:file", "bind": "jar/org/typelevel/cats_kernel_2_12"},
    {"artifact": "org.typelevel:cats-macros_2.12:2.0.0", "lang": "scala", "sha1": "5b86236c7f39ae44a5b26b552283fe678aaff449", "sha256": "f730fcd0d679e7e13a56a2b5777f53204456a934d90ffc3be4d6bb10ce19919a", "repository": "https://repo.maven.apache.org/maven2/", "url": "https://repo.maven.apache.org/maven2/org/typelevel/cats-macros_2.12/2.0.0/cats-macros_2.12-2.0.0.jar", "source": {"sha1": "aaded5d8dc45e1ae9fd2f41045dacb60df5ff9cc", "sha256": "ef8a1aeb76e02f62f3b546ff74734da3991d304552bbcef1715f34dd02160049", "repository": "https://repo.maven.apache.org/maven2/", "url": "https://repo.maven.apache.org/maven2/org/typelevel/cats-macros_2.12/2.0.0/cats-macros_2.12-2.0.0-sources.jar"} , "name": "org_typelevel_cats_macros_2_12", "actual": "@org_typelevel_cats_macros_2_12//jar:file", "bind": "jar/org/typelevel/cats_macros_2_12"},
    {"artifact": "org.typelevel:jawn-parser_2.12:0.14.2", "lang": "scala", "sha1": "47c7bbb52a7b5c6b652c9ca8fcd60dc5e3583c2c", "sha256": "0b95b9089ec3bb42384abff52e0e3087a05710a69b50d708e106ab280684a270", "repository": "https://repo.maven.apache.org/maven2/", "url": "https://repo.maven.apache.org/maven2/org/typelevel/jawn-parser_2.12/0.14.2/jawn-parser_2.12-0.14.2.jar", "source": {"sha1": "5bcc7d5b81693ab5244834d7fa98f28eba383466", "sha256": "fe6e9bdb096fa8a1eddc80b9ea9619b25f17f7fd34d395cc2b675c4e97e18959", "repository": "https://repo.maven.apache.org/maven2/", "url": "https://repo.maven.apache.org/maven2/org/typelevel/jawn-parser_2.12/0.14.2/jawn-parser_2.12-0.14.2-sources.jar"} , "name": "org_typelevel_jawn_parser_2_12", "actual": "@org_typelevel_jawn_parser_2_12//jar:file", "bind": "jar/org/typelevel/jawn_parser_2_12"},
    {"artifact": "org.typelevel:machinist_2.12:0.6.6", "lang": "scala", "sha1": "4086874ad28be846916347dd74ba5395c63eaf50", "sha256": "e1f56da37a817ff9b20e36870ac39b64277155e82cd2cde25d76d10af14b8a96", "repository": "https://repo.maven.apache.org/maven2/", "url": "https://repo.maven.apache.org/maven2/org/typelevel/machinist_2.12/0.6.6/machinist_2.12-0.6.6.jar", "source": {"sha1": "1aafa2c22a3fb1e2c1385a1c2b796340a166ff0b", "sha256": "b8b693aa0a844dd095550e9a2ce63870d605fdcc16447261ec063e80e0b4795c", "repository": "https://repo.maven.apache.org/maven2/", "url": "https://repo.maven.apache.org/maven2/org/typelevel/machinist_2.12/0.6.6/machinist_2.12-0.6.6-sources.jar"} , "name": "org_typelevel_machinist_2_12", "actual": "@org_typelevel_machinist_2_12//jar:file", "bind": "jar/org/typelevel/machinist_2_12"},
    {"artifact": "org.typelevel:paiges-core_2.12:0.3.0", "lang": "scala", "sha1": "c1d6e4305c90b232810f95323d8be8d2aff97335", "sha256": "c3e272022fd6389c6104e9a407218eb2cfcd2299e8b2860e7a59a0441f3fd4a4", "repository": "https://repo.maven.apache.org/maven2/", "url": "https://repo.maven.apache.org/maven2/org/typelevel/paiges-core_2.12/0.3.0/paiges-core_2.12-0.3.0.jar", "source": {"sha1": "9989f847989fe3991a26c2e3d8e1a23cd6f15e73", "sha256": "e3c0a7151f0e346e25c08d59027e2c50def853e9a28f08584aa84588d8e7b643", "repository": "https://repo.maven.apache.org/maven2/", "url": "https://repo.maven.apache.org/maven2/org/typelevel/paiges-core_2.12/0.3.0/paiges-core_2.12-0.3.0-sources.jar"} , "name": "org_typelevel_paiges_core_2_12", "actual": "@org_typelevel_paiges_core_2_12//jar:file", "bind": "jar/org/typelevel/paiges_core_2_12"},
    ]

def maven_dependencies(callback = jar_artifact_callback):
    for hash in list_dependencies():
        callback(hash)
