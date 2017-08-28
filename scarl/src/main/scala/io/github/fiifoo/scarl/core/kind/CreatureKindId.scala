package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.Creature

case class CreatureKindId(value: String) extends KindId[Creature] {
  def apply(s: State): CreatureKind = s.assets.kinds.creatures(this)
}
