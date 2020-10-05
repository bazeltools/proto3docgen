package io.bazeltools.proto3docgen.repr

import io.circe.derivation.deriveDecoder
import io.circe._, io.circe.parser._
import io.bazeltools.proto3docgen.md.{Section, Sectionable, Table, Enrichments}
import io.bazeltools.proto3docgen.context.ProtoContext

case class Service(
  name: String,
  longName: String,
  fullName: String,
  description: String,
  methods: Option[List[Method]],
)

object Service {
  import Enrichments._

  implicit val decoder: Decoder[Service] = deriveDecoder[Service]
  implicit val sec: Sectionable[Service] = new Sectionable[Service] {
    def toSection(s: Service)(implicit ctx: ProtoContext) =
      Section()
        .withName(ctx.renderTypeSrc(s.longName, s.fullName))
        .withDescription(s.description)
        .withInnerSection {
          import Method._
          Sectionable
            .toSection(s.methods.getOrElse(List.empty))
            .withName("Methods")
        }
  }
}

case class Method(
  name: String,
  description: String,
  requestType: String,
  requestLongType: String,
  requestFullType: String,
  requestStreaming: Boolean,
  responseType: String,
  responseLongType: String,
  responseFullType: String,
  responseStreaming: Boolean
) {
  def toRow(implicit ctx: ProtoContext): List[(Symbol, String)] =
    List(
      ('Name, name),
      ('Request, ctx.renderType(requestFullType)),
      ('Response, ctx.renderType(responseFullType)),
      ('Description, description)
    )
}

object Method {
  import Enrichments._

  implicit val decoder: Decoder[Method] = deriveDecoder[Method]
  implicit val sec: Sectionable[List[Method]] = new Sectionable[List[Method]] {
    def toSection(methods: List[Method])(implicit ctx: ProtoContext): Section =
      Section()
        .withContent(Table.toMarkdown(methods.map(_.toRow)))
        .withName("Extensions")
  }
}
