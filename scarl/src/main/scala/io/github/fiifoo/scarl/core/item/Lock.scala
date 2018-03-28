package io.github.fiifoo.scarl.core.item

import io.github.fiifoo.scarl.core.entity.CreatureId

case class Lock(key: Option[Key] = None, security: Int = 0)

object Lock {

  def apply(source: Source, owner: Option[CreatureId]): Lock = {
    val key: Option[Key] = source.key orElse (owner map (owner => {
      PrivateKey(owner)
    }))

    Lock(key, source.security)
  }

  case class Source(key: Option[SharedKey] = None, security: Int = 0)

}
