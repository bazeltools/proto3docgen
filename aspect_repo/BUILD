java_import(name="markdown_jar", jars = ["GenerateMarkdown_deploy.jar"])

java_binary(name="MarkdownGen", runtime_deps = [":markdown_jar"], main_class="io.bazeltools.proto3docgen.GenerateMarkdown")



java_import(name="ExtractOutputGroups_jar", jars = ["ExtractOutputGroups_deploy.jar"])

java_binary(name="ExtractOutputGroups", runtime_deps = [":ExtractOutputGroups_jar"], main_class="io.bazeltools.proto3docgen.extract_output_groups.ExtractOutputGroups")


