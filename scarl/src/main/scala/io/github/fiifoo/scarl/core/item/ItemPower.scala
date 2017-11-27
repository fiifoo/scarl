package io.github.fiifoo.scarl.core.item

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}

trait ItemPower {
  def apply(s: State, item: ItemId, user: Option[CreatureId] = None): List[Effect]
}
