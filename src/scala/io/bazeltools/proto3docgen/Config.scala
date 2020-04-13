package io.bazeltools.proto3docgen

import scopt.OParser

case class Config(
  jsonPath: String = "",
  githubPrefix: String = "",
  renderingEngine: Option[Rendering.Engine] = None
)

object Config {
  val builder = OParser.builder[Config]
  val parser = {
  import builder._
    OParser.sequence(
      programName("proto3docgen"),
      head("proto3docgen", "0.0.0"),
      opt[String]('j', "json-path").required()
        .text("File with paths to parse")
        .action((jsonPath, cfg) => cfg.copy(jsonPath = jsonPath)),
      opt[String]('g', "github-prefix").required()
        .text("Github prefix (e.g. http://github.com/foo/bar)")
        .action((ghp, cfg) => cfg.copy(githubPrefix = ghp)),
      opt[String]('e', "rendering-engine").required()
        .text("Rendering engine to use. Supported: hugo")
        .validate {re =>
          Rendering.Engine.byName(re) match {
            case Some(_) => Right()
            case None => Left("Invalid engine")
          }
        }
        .action((re, cfg) => cfg.copy(renderingEngine = Rendering.Engine.byName(re))
      )
    )
  }

  def parse(args: Array[String]): Config =
    OParser.parse(parser, args, Config()) match {
      case Some(c) => c
      case None =>
        System.exit(-1)
        ???
    }
}
