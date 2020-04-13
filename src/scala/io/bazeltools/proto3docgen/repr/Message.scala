package io.bazeltools.proto3docgen.repr

import io.circe.derivation.deriveDecoder
import io.circe._, io.circe.parser._
import io.bazeltools.proto3docgen.md.{Section, Sectionable, Table, Enrichments}

case class ProtoLocationMap(
  longName: String,
  `package`: String,
) {
  def ref = s"${`package`}/#${longName.toLowerCase.replaceAll("[^a-z]+", "")}"
}

case class Message(
  name: String,
  longName: String,
  fullName: String,
  description: String,
  hasExtensions: Boolean,
  hasFields: Boolean,
  extensions: List[Extension],
  fields: List[Field]
)

object Message {
  implicit val decoder: Decoder[Message] = deriveDecoder[Message]
  implicit val sec: Sectionable[Message] = new Sectionable[Message] {
    def toSection(s: Message)(implicit ctx: ProtoContext) =
      Section()
        .withName(ctx.renderTypeSrc(s.longName))
        .withDescription(s.description)
        .withInnerSection {
          import Field._
          Sectionable.toSection(s.fields)
        }.withInnerSection {
          import Extension._
          Sectionable.toSection(s.extensions)
        }
  }
}

case class Field(
  name: String,
  description: String,
  label: String,
  `type`: String,
  longType: String,
  fullType: String,
  ismap: Boolean,
  defaultValue: String
) {
  def toRow()(implicit ctx: ProtoContext): List[(Symbol, String)] = List(
    ('Name, name),
    ('Type, ctx.renderType(fullType)),
    ('Label, label),
    ('Description, description),
  )
}

object Field {
  implicit val decoder: Decoder[Field] = deriveDecoder[Field]
  implicit val sec: Sectionable[List[Field]] = new Sectionable[List[Field]] {
    def toSection(fields: List[Field])(implicit ctx: ProtoContext): Section =
      Section()
        .withContent(Table.toMarkdown(fields.map(_.toRow)))
        .withName("Fields")
  }
}
