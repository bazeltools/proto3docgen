package io.bazeltools.proto3docgen.md

import org.typelevel.paiges.Doc
import io.bazeltools.proto3docgen.context.ProtoContext

case class Section(
  name: Option[String] = None,
  description: Option[String] = None,
  innerRev: List[Either[Section, Doc]] = List(),
) {

  def withName(n: String) =
    copy(name = Option(n).filter(_.nonEmpty))

  def withDescription(n: String) =
    copy(description = Option(n).filter(_.nonEmpty))

  def toMarkdown(level: Integer): Doc = {
    val contents = List(
      description.map {s => Doc.text(s) + Doc.hardLine}.toList,
      innerRev.reverse.map {
        case Right(doc) => doc
        case Left(sec) => sec.toMarkdown(level + 1)}
    ).flatten.filter(_.nonEmpty)

    if (contents.isEmpty)
      Doc.empty
    else
      Doc.intercalate(
        Doc.hardLine,
        List(name.map(Heading(level)(_)).toList, contents).flatten,
      )
  }

  def withContent(doc: Doc) = copy(innerRev = Right(doc) :: innerRev)

  def withInnerSection(section: Section): Section = {
    copy(innerRev = Left(section) :: innerRev)
  }
}
