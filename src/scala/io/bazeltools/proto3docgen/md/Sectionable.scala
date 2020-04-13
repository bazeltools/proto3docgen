package io.bazeltools.proto3docgen.md

import org.typelevel.paiges.Doc
import io.bazeltools.proto3docgen.repr.ProtoContext
trait Sectionable[T] {
  def toSection(t: T)(implicit ctx: ProtoContext): Section
}
object Sectionable {
  def toSection[T: Sectionable](t: T)(implicit ctx: ProtoContext): Section = {
    implicitly[Sectionable[T]].toSection(t)
  }

  implicit val sec: Sectionable[Section] = new Sectionable[Section] {
    def toSection(t: Section)(implicit ctx: ProtoContext): Section = t
  }
}
object Enrichments {
  implicit class SE[T : Sectionable](val l: List[T]) {
    def toSection(implicit ctx: ProtoContext): Section =
      l.foldLeft(Section()) {case (sec, el) =>
        sec.withInnerSection(Sectionable.toSection(el))
      }
  }
}
