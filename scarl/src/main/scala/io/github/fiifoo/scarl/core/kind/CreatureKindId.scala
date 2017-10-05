package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.State

case class CreatureKindId(value: String) extends KindId {
  def apply(s: State): CreatureKind = s.assets.kinds.creatures(this)
}
