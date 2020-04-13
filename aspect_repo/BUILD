java_import(name="markdown_jar", jars = ["GenerateMarkdown_deploy.jar"])

java_binary(name="markdown_gen", runtime_deps = [":markdown_jar"], main_class="io.bazeltools.proto3docgen.GenerateMarkdown")
