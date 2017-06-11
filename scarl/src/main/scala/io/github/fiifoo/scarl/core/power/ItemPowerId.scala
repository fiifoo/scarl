package io.github.fiifoo.scarl.core.power

import io.github.fiifoo.scarl.core.State

case class ItemPowerId(value: String) extends PowerId {
  def apply(s: State): ItemPower = s.powers.items(this)
}
