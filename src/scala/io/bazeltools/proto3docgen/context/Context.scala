package io.bazeltools.proto3docgen.context

trait ProtoContext {
  def withFileName(fn: String): ProtoContext
  def withActivePackage(pkgName: String): ProtoContext
  def renderType(fullName: String): String
  def renderTypeSrc(fullName: String): String
}
