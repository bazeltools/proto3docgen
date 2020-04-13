package io.bazeltools.proto3docgen.repr

import io.circe.derivation.deriveDecoder
import io.circe._, io.circe.parser._
import io.bazeltools.proto3docgen.md.{Section, Sectionable, Table, Enrichments}

case class Extension(
  name: String,
  longName: String,
  fullName: String,
  description: String,
  label: String,
  `type`: String,
  longType: String,
  fullType: String,
  number: Long,
  defaultValue: String,
  containingType: String,
  containingLongType: String,
  containingFullType: String
) {
  def toRow: List[(Symbol, String)] = List(
    ('Name, longName),
    ('Type, longType),
    ('Description, description),
  )
}

object Extension {
  import Enrichments._

  implicit val decoder: Decoder[Extension] = deriveDecoder[Extension]

  implicit val sec: Sectionable[List[Extension]] = new Sectionable[List[Extension]] {
    def toSection(ex: List[Extension])(implicit ctx: ProtoContext): Section =
      Section()
        .withContent(Table.toMarkdown(ex.map(_.toRow)))
        .withName("Extensions")
  }
}
