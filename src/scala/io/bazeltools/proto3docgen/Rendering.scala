package io.bazeltools.proto3docgen

import io.bazeltools.proto3docgen.repr.ProtoPackage
import io.bazeltools.proto3docgen.context.ProtoContext
import java.nio.file.Files
import java.nio.file.Path

object Rendering {
  trait Engine {
    def name: String
  }

  object Engine {
    def byName(s: String): Option[Engine] =
      List(Hugo).find {e => e.name.toLowerCase == s.toLowerCase}

    case object Hugo extends Engine {
      def name = "Hugo"
    }
  }
}
