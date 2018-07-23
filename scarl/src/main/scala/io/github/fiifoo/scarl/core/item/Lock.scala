package io.github.fiifoo.scarl.core.item

import io.github.fiifoo.scarl.core.entity.CreatureId

case class Lock(key: Option[Key] = None, security: Int = 0, sub: Option[Lock] = None)

object Lock {

  def apply(source: Source, owner: Option[CreatureId]): Lock = {
    val key = source.key orElse (owner map (owner => {
      PrivateKey(owner)
    }))

    val sub = source.sub map (sub => Lock(sub, owner))

    Lock(key, source.security, sub)
  }

  case class Source(key: Option[SharedKey] = None, security: Int = 0, sub: Option[Source] = None)

}
