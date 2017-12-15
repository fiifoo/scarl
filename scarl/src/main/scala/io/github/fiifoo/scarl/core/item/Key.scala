package io.github.fiifoo.scarl.core.item

import io.github.fiifoo.scarl.core.entity.CreatureId

sealed trait Key

object PrivateKey {
  def apply(owner: CreatureId): PrivateKey = PrivateKey(owner.value)
}

case class PrivateKey(value: Int) extends Key

case class SharedKey(kind: KeyKindId) extends Key

case class KeyKindId(value: String)

case class KeyKind(id: KeyKindId, name: String)
