package io.bazeltools.proto3docgen

import io.bazeltools.proto3docgen.repr.{ProtoPackage, ProtoContext}
import java.nio.file.Files
import java.nio.file.Path

object Rendering {
  trait Engine {
    def name: String
  }

  object Engine {
    def byName(s: String): Option[Engine] =
      List(Hugo).find {e => e.name.toLowerCase == s.toLowerCase}

    case object Hugo extends Engine {
      def name = "Hugo"
    }
  }

  case class HugoContext(
    prefix: String,
    fileName: Option[String],
    typeMap: Map[String, (String, String)],
    outputRoot: Path,
    layoutMode: LayoutMode
  ) extends ProtoContext {
    def pkgToUrl(pkg: String): String = layoutMode match {
        case LayoutMode.Nested => pkg.split('.').mkString("/")
        case LayoutMode.UnNested => pkg
      }

    Files.createDirectories(outputRoot)

    def withFileName(fn: String): ProtoContext =
      copy(fileName = Some(fn))

    def renderType(fullName: String): String = typeMap.get(fullName) match {
      case Some((protoPkg, longName)) =>
        s"""[$longName]({{< ref "${pkgToUrl(protoPkg)}.md#${longName.toLowerCase.replaceAll("[^a-z]+", "")}" >}})"""
      case None => s"`$fullName`"
    }

    def renderTypeSrc(fullName: String): String =
      fileName match {
        case Some(fn) => s"[$fullName]($prefix/$fn) {#${fullName.toLowerCase.replaceAll("[^a-z]+", "")}}"
        case None => fullName
      }

    def writePackage(pkg: ProtoPackage): Unit = {
            val contents = s"""
---
title: ${pkg.name}
---

${pkg.toSection(this).toMarkdown(1).render(0)}
"""

      val outputP = pkgToUrl(pkg.name)
      val outputFolder = outputRoot.resolve(outputP).getParent
      if(!outputFolder.toFile.exists) {
        Files.createDirectories(outputFolder)
        layoutMode match {
          case LayoutMode.Nested =>
            Files.write(outputFolder.resolve("_index.md"), s"""
            |---
            |title: "${outputP.split('/').last}"
            |---
            |""".stripMargin.getBytes)
          case _ => ()
        }
      }
      Files.write(
        outputRoot.resolve(s"${outputP}.md"),
        contents.getBytes
      )
    }

  }
}
