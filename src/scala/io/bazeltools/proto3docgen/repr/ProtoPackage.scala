package io.bazeltools.proto3docgen.repr

import io.bazeltools.proto3docgen.md.{Section, Sectionable, Table, Enrichments}
import io.bazeltools.proto3docgen.context.ProtoContext

case class ProtoPackage(
  name: String,
  files: List[File],
) {
  import Enrichments._

  def toInnerSection[T: Sectionable](sectionName: String)(ex: File => List[T])(implicit outerCtx: ProtoContext): Section = {
    files.map { f =>
      val innerCtx = outerCtx.withFileName(f.name)
      ex(f).toSection(innerCtx)
    }.toSection.withName(sectionName)
  }

  def toSection(implicit ctx: ProtoContext): Section = {
    privToSection(ctx.withActivePackage(name))
  }

  private[this] def privToSection(implicit ctx: ProtoContext): Section = {
    import Enrichments._

    Section()
      .withInnerSection {
        import Extension._
        toInnerSection("Extensions")(t => List(t.extensions))
      }
      .withInnerSection {
        toInnerSection("Enums")(_.enums)
      }
      .withInnerSection(
        toInnerSection("Messages")(_.messages)
      )
      .withInnerSection(
        toInnerSection("Services")(_.services)
      )
  }
}
