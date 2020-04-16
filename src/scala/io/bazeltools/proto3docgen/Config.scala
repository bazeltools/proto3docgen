package io.bazeltools.proto3docgen

import scopt.OParser
import java.nio.file.{Paths, Path}
case class Config(
  jsonPath: String = "",
  githubPrefix: String = "",
  renderingEngine: Option[Rendering.Engine] = None,
  outputRoot: Path = null,
  layoutMode: LayoutMode = LayoutMode.UnNested
)

sealed trait LayoutMode
object LayoutMode {
  def apply(s: String): LayoutMode = s match {
    case "unnested" => UnNested
    case "nested" => Nested
  }
  case object UnNested extends LayoutMode
  case object Nested extends LayoutMode
}
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
      opt[String]('o', "output-root").required()
        .text("Output root, where you want the files built by the rendering engine to go")
        .action((p, cfg) => cfg.copy(outputRoot = Paths.get(p))),
      opt[String]('e', "rendering-engine").required()
        .text("Rendering engine to use. Supported: hugo")
        .validate {re =>
          Rendering.Engine.byName(re) match {
            case Some(_) => Right()
            case None => Left("Invalid engine")
          }
        }
        .action((re, cfg) => cfg.copy(renderingEngine = Rendering.Engine.byName(re))),
      opt[String]('l', "layout-mode")
        .text("Layout mode too use: ")
        .validate {re =>
          re match {
            case "unnested" => Right(())
            case "nested" => Right(())
            case o => Left(s"Invalid layout mode: $re")
          }
        }
        .action((re, cfg) => cfg.copy(layoutMode = LayoutMode(re))
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
