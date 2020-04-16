package io.bazeltools.proto3docgen

import io.circe._, io.circe.parser._
import io.bazeltools.proto3docgen.repr.{File, ProtodocJson}
import io.bazeltools.proto3docgen.context.ProtoContext
import java.nio.file.Paths
import scala.io.Source

object GenerateMarkdown {
  def main(args: Array[String]): Unit = {
    val config = Config.parse(args)
    val parsedFiles: List[File] = Source.fromFile(config.jsonPath).getLines.flatMap { e =>
      val contents = Source.fromFile(e).mkString
      ProtodocJson.parseFile(contents)
    }.toList

    val packageList = ProtodocJson.toProtoPackage(parsedFiles)
    val typeMap = packageList.flatMap {p =>
      p.files.flatMap(_.messages).map {m =>
        (m.fullName, (p.name, m.longName))
      } ++ p.files.flatMap(_.enums).map {e =>
        (e.fullName, (p.name, e.longName))
      }
    }.toMap

    implicit val ctx = HugoContext(
      prefix = s"${config.githubPrefix}/blob/master",
      fileName = None,
      typeMap = typeMap,
      outputRoot = config.outputRoot,
      layoutMode = config.layoutMode
    )
    packageList.foreach(ctx.writePackage(_))
  }
}
