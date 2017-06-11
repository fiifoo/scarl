package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import io.github.fiifoo.scarl.effect.{DropItemEffect, TickEffect, UnequipItemEffect}

case class DropItemAction(item: ItemId) extends Action {
  val cost = 100

  def apply(s: State, actor: CreatureId): List[Effect] = {
    val equipped = s.equipments.get(actor) exists (_.values exists (_ == item))

    if (equipped) {
      List(
        TickEffect(actor, cost),
        UnequipItemEffect(actor, item),
        DropItemEffect(item, actor)
      )
    } else {
      List(
        TickEffect(actor, cost),
        DropItemEffect(item, actor)
      )
    }
  }
}
