package io.github.fiifoo.scarl.core.item

case class Lock(key: Option[Key] = None, security: Int = 0)

object Lock {

  def apply(source: Source): Lock = {
    Lock(source.key, source.security)
  }

  case class Source(key: Option[SharedKey] = None, security: Int = 0)

}
