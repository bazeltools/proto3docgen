package io.bazeltools.proto3docgen.repr

import io.circe.derivation.deriveDecoder
import io.circe._, io.circe.parser._
import io.bazeltools.proto3docgen.md.{Section, Sectionable, Table, Enrichments}

case class File(
  name: String,
  description: String,
  `package`: String,
  hasEnums: Boolean,
  hasExtensions: Boolean,
  hasMessages: Boolean,
  hasServices: Boolean,
  enums: List[Enum],
  extensions: List[Extension],
  messages: List[Message],
  services: List[Service]
) {
  import Enrichments._

  def shortName: String = name.split("/").last

  def toSection(implicit ctx: ProtoContext): Section = {
    import Enrichments._

    Section()
      .withName(shortName)
      .withDescription(description)
      .withInnerSection {
        import Extension._
        Sectionable.toSection(extensions).withName("Extensions")
      }
      .withInnerSection {
        enums.toSection.withName("Enums")
      }
      .withInnerSection(
        messages.toSection.withName("Messages")
      )
      .withInnerSection(
        services.toSection.withName("Services")
      )
    }
}

object File {
  implicit val decoder: Decoder[File] = deriveDecoder[File]
}
