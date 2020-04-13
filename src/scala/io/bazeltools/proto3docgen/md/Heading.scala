package io.bazeltools.proto3docgen.md

import org.typelevel.paiges.Doc

case class Heading(level: Integer) {
  def apply(str: String) = Doc.hardLine + Doc.text("#"*level) & Doc.text(str) + Doc.hardLine
  def next = Heading(level + 1)
}
