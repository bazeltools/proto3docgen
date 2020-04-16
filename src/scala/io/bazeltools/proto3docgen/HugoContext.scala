package io.bazeltools.proto3docgen

import io.bazeltools.proto3docgen.repr.ProtoPackage
import io.bazeltools.proto3docgen.context.ProtoContext
import java.nio.file.Files
import java.nio.file.{Path, Paths}

object HugoContext {
  def stripExtension(fname: String): String = {
    val lastIdx = fname.lastIndexOf('.')
    if(lastIdx > 0) {
      fname.substring(0, lastIdx - 1)
    } else fname
  }

  implicit class PathHelper(val p: Path) extends AnyVal {
    def basenameWithoutExtension: String =
      stripExtension(p.toFile.getName)
  }
}
case class HugoContext(
  prefix: String,
  fileName: Option[String],
  currentPackageNameOpt: Option[String] = None,
  typeMap: Map[String, (String, String)],
  outputRoot: Path,
  layoutMode: LayoutMode
) extends ProtoContext {
  import HugoContext._
  def pkgToPath(pkg: String): Path = layoutMode match {
      case LayoutMode.Nested => Paths.get(s"${pkg.split('.').mkString("/")}.md")
      case LayoutMode.UnNested => Paths.get(s"${pkg}.md")
    }

  Files.createDirectories(outputRoot)

  def withFileName(fn: String): ProtoContext =
    copy(fileName = Some(fn))


  def withActivePackage(pkgName: String): ProtoContext =
    copy(currentPackageNameOpt = Some(pkgName))

  def currentPackageName = currentPackageNameOpt.getOrElse(sys.error(s"Invalid state, pkg name is unset :/"))

  def renderType(fullName: String): String = typeMap.get(fullName) match {
    case Some((protoPkg, longName)) =>
      val targetPackage = pkgToPath(protoPkg)
      val srcPackage = pkgToPath(currentPackageName).getParent // get the folder that the src package is in

      s"""[$longName]({{< ref "${srcPackage.relativize(targetPackage)}.md#${longName.toLowerCase.replaceAll("[^a-z]+", "")}" >}})"""
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

    val outputP = pkgToPath(pkg.name)
    val outputFolder = outputRoot.resolve(outputP).getParent
    if(!outputFolder.toFile.exists) {
      Files.createDirectories(outputFolder)
      layoutMode match {
        case LayoutMode.Nested =>
          Files.write(outputFolder.resolve("_index.md"), s"""
          |---
          |title: "${outputP.basenameWithoutExtension}"
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
