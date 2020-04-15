package io.bazeltools.proto3docgen.extract_output_groups

import java.nio.file.Path
import scala.util.Try
import java.io.FileInputStream
import com.google.devtools.build.lib.buildeventstream.BuildEventStreamProtos.BuildEvent
import scala.collection.JavaConverters._
import com.google.protobuf.ByteString
import java.nio.file.Paths

// case class TargetOutputs(outputGroups: )
case class NamedSetOfFiles(
  id: String,
  files: List[Either[Path, ByteString]],
  subTypes: List[String]
)
object ExtractOutputGroups {
  // : Try[Map[String, List[String]]]
  def apply(path: Path): Try[List[Either[java.nio.file.Path,com.google.protobuf.ByteString]]] = Try {
    val is = new FileInputStream(path.toFile)

    val buildEvents: List[BuildEvent] = try {
      @annotation.tailrec
      def go(acc: List[BuildEvent]): List[BuildEvent] = {
        Option(BuildEvent.parseDelimitedFrom(is)) match {
          case None => acc.reverse
          case Some(ex) => go(ex :: acc)
        }
      }
      go(Nil)
    } finally {
      is.close()
    }

    val avalableFileSets = buildEvents.flatMap { e =>
      Option(e.getId.getNamedSet).flatMap { id =>
        Option(e.getNamedSetOfFiles).map { p =>
          val fileData = p.getFilesList().asScala.map { f =>
            Option(f.getUri).filter(_.nonEmpty).map(l => Left(Paths.get(new java.net.URL(l).toURI))).getOrElse(Right(f.getContents))
          }.toList

          val subIds = p.getFileSetsList.asScala.map(_.getId).toList

          NamedSetOfFiles(
            id = id.getId,
            files = fileData,
            subTypes = subIds)
        }
      }
    }.groupBy(_.id).map { case (k, v) =>
      require(v.distinct.size == 1, s"For id : '$k', saw : $v")
      (k,v.head)
    }


    buildEvents.flatMap { e =>
      Option(e.getCompleted)
    }.map { target =>
      target.getOutputGroupList.asScala.filter(_.getName == "runtime_classpath").flatMap { e => e.getFileSetsList.asScala }.flatMap { e =>
        @annotation.tailrec
        def go(pendingIds: List[String], acc: List[Either[Path, ByteString]]): List[Either[Path, ByteString]] = {
          pendingIds match {
            case Nil => acc
            case h :: t =>
                val nxt = avalableFileSets(h)
                go((t ++ nxt.subTypes).distinct, acc ++ nxt.files)
          }
        }
        go(List(e.getId), Nil)
      }
    }.flatMap(identity)
  }


  def main(args: Array[String]): Unit = {
    val inputPath = Paths.get(args(0))
    val outputPath = Paths.get(args(1))


    val data = apply(inputPath).get

    val pw = new java.io.PrintWriter(outputPath.toFile)
    try {
      data.foreach {
          case Left(p) => pw.println(p)
          case Right(ex) => sys.error("Unsupported inline file output today")
        }
    } finally {
      pw.close()
    }
  }
}
