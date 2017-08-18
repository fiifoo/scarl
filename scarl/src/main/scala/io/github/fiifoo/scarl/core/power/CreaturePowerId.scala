package io.github.fiifoo.scarl.core.power

import io.github.fiifoo.scarl.core.State

case class CreaturePowerId(value: String) extends PowerId {
  def apply(s: State): CreaturePower = s.assets.powers.creatures(this)
}
