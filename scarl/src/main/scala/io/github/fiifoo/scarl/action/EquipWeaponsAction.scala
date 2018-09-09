package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import io.github.fiifoo.scarl.core.item.Equipment.Slot
import io.github.fiifoo.scarl.effect.TickEffect
import io.github.fiifoo.scarl.effect.interact.EquipWeaponsEffect

case class EquipWeaponsAction(weapons: Map[Slot, ItemId]) extends Action {
  def apply(s: State, actor: CreatureId): List[Effect] = {
    List(
      TickEffect(actor),
      EquipWeaponsEffect(actor, this.weapons)
    )
  }
}
