package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import io.github.fiifoo.scarl.core.equipment.Slot
import io.github.fiifoo.scarl.effect.{EquipItemEffect, TickEffect}

case class EquipItemAction(item: ItemId, slot: Slot) extends Action {
  val cost = 100

  def apply(s: State, actor: CreatureId): List[Effect] = {
    List(
      TickEffect(actor, cost),
      EquipItemEffect(actor, item, slot)
    )
  }
}