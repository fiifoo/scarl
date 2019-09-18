package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import io.github.fiifoo.scarl.effect.TickEffect
import io.github.fiifoo.scarl.effect.interact.{DropItemEffect, UnequipItemEffect}

case class DropItemAction(item: ItemId) extends Action {
  def apply(s: State, actor: CreatureId): List[Effect] = {
    val equipped = s.creature.equipments.get(actor) exists (_.values exists (_ == item))
    val location = actor(s).location

    if (equipped) {
      List(
        TickEffect(actor),
        UnequipItemEffect(actor, item, location),
        DropItemEffect(item, actor, location)
      )
    } else {
      List(
        TickEffect(actor),
        DropItemEffect(item, actor, location)
      )
    }
  }
}
