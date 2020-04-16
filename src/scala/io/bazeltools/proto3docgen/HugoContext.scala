package io.bazeltools.proto3docgen

import io.bazeltools.proto3docgen.repr.ProtoPackage
import io.bazeltools.proto3docgen.context.ProtoContext
import java.nio.file.Files
import java.nio.file.{Path, Paths}

object HugoContext {
  def stripExtension(fname: String): String = {
    val lastIdx = fname.lastIndexOf('.')
    if(lastIdx > 0) {
      fname.substring(0, lastIdx)
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
      case LayoutMode.Nested => Paths.get(s"${pkg.replace('.','/')}/_index.md")
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
      val srcPackageOpt = Option(pkgToPath(currentPackageName).getParent) // get the folder that the src package is in

      val pathToTarget = srcPackageOpt match {
        case None => targetPackage
        case Some(srcPackage) => srcPackage.relativize(targetPackage)
      }
      s"""[$longName]({{< ref "${pathToTarget}#${longName.toLowerCase.replaceAll("[^a-z]+", "")}" >}})"""
    case None => s"`$fullName`"
  }

  def renderTypeSrc(longName: String, fullName: String): String =
    fileName match {
      case Some(fn) => s"[$longName]($prefix/$fn) {#${longName.toLowerCase.replaceAll("[^a-z]+", "")}}\n${fullName}"
      case None => s"${longName} ${fullName} {#${longName.toLowerCase.replaceAll("[^a-z]+", "")}}\n${fullName}"
    }

  private[this] def initializeParents(outputP: Path): Unit = {
    Option(outputP.getParent).foreach { parentPath =>
      val segments = parentPath.toString.split('/').toList
      val root = outputRoot
      @annotation.tailrec
      def go(usedSegments: List[String], remaining: List[String]): Unit = {
        remaining match {
          case h :: t =>
            val nextUsed = usedSegments :+ h
            val nxtFolder  = root.resolve(nextUsed.mkString("/"))
            Files.createDirectories(nxtFolder)
            val indexFilePath = nxtFolder.resolve("_index.md")
            if(!indexFilePath.toFile.exists) {
              Files.write(indexFilePath, s"""
              |---
              |title: "${nextUsed.mkString(".")}"
              |linktitle: "${h}"
              |---
              |""".stripMargin.getBytes)
            }
            go(nextUsed, t)
          case Nil => ()
        }
      }
      go(Nil, segments)
    }
  }


  def writePackage(pkg: ProtoPackage): Unit = {
    val pkgTitle = layoutMode match {
      case LayoutMode.Nested => pkg.name.split('.').last
      case LayoutMode.UnNested => pkg.name
    }
          val contents = s"""
---
title: "${pkg.name}"
linkTitle: "${pkgTitle}"
---

${pkg.toSection(this).toMarkdown(1).render(0)}
"""

    val outputP = pkgToPath(pkg.name)
    initializeParents(outputP)
    Files.write(
      outputRoot.resolve(outputP),
      contents.getBytes
    )
  }

}
