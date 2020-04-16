package io.bazeltools.proto3docgen.repr

import io.circe.derivation.deriveDecoder
import io.circe._, io.circe.parser._
import io.bazeltools.proto3docgen.md.{Section, Sectionable, Table, Enrichments}
import io.bazeltools.proto3docgen.context.ProtoContext

case class ProtodocJson(
  files: List[File]
)
object ProtodocJson {
  import Enrichments._
  implicit val decoder: Decoder[ProtodocJson] = deriveDecoder[ProtodocJson]

  def parseFile(input: String): List[File] = {
    decode[ProtodocJson](input) match {
      case Right(json) =>
        json.files

      case Left(ex) => throw ex
    }
  }

  def toProtoPackage(files: List[File]): List[ProtoPackage] =
    files.groupBy(_.`package`).map { case (k, l) =>
      ProtoPackage(k, l)
    }.toList
}
