package io.bazeltools.proto3docgen.repr

trait ProtoContext {
  def withFileName(fn: String): ProtoContext
  def renderType(fullName: String): String
  def renderTypeSrc(fullName: String): String
}
