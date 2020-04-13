package io.bazeltools.proto3docgen.md

import org.typelevel.paiges.Doc

object Table {
  implicit class TableEnrichments(val doc: Doc) {
    def asTableRow = Doc.char('|') + doc + Doc.char('|')
  }

  def toMarkdown(rows: List[List[(Symbol, String)]]): Doc = rows.headOption match {
    case None => Doc.empty
    case Some(examplar) =>
      val headings = examplar.map(_._1.name)
      val headingRows = List(
          Doc.intercalate(Doc.char('|'), headings.map(Doc.text(_))),
          Doc.intercalate(Doc.char('|'), headings.map(_ => Doc.char('-'))),
        )
      val contentRows = rows.map {row =>
        Doc.intercalate(
          Doc.char('|'),
          row.map(el => Doc.text(el._2.replace("\n", " ")))
        )
      }

      Doc.intercalate(
        Doc.hardLine,
        (headingRows ++ contentRows).map(_.asTableRow)
      ) + Doc.hardLine
  }
}
