package io.bazeltools.proto3docgen.repr

import io.circe.derivation.deriveDecoder
import io.circe._, io.circe.parser._
import io.bazeltools.proto3docgen.md.{Section, Sectionable, Table}
import io.bazeltools.proto3docgen.context.ProtoContext

case class Enum(
  name: String,
  longName: String,
  fullName: String,
  description: String,
  values: List[EnumValue]
)

object Enum {
  implicit val decoder: Decoder[Enum] = deriveDecoder[Enum]
  implicit val sec: Sectionable[Enum] = new Sectionable[Enum] {
    def toSection(s: Enum)(implicit ctx: ProtoContext) =
      Section()
        .withName(ctx.renderTypeSrc(s.longName))
        .withDescription(s.description)
        .withInnerSection {
          import EnumValue._
          Sectionable.toSection(s.values)
        }
  }
}

case class EnumValue(
  name: String,
  number: Long,
  description: String
) {
  def toRow: List[(Symbol, String)] = List(
    ('Number, number.toString),
    ('Name, name),
    ('Description, description),
  )
}

object EnumValue {
  implicit val decoder: Decoder[EnumValue] = deriveDecoder[EnumValue]
  implicit val sec: Sectionable[List[EnumValue]] = new Sectionable[List[EnumValue]] {
    def toSection(enumValues: List[EnumValue])(implicit ctx: ProtoContext): Section =
      Section()
        .withContent(Table.toMarkdown(enumValues.map(_.toRow)))
        .withName("Fields")
  }
}

