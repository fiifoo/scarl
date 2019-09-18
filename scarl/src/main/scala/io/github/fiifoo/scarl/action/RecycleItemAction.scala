package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import io.github.fiifoo.scarl.effect.TickEffect
import io.github.fiifoo.scarl.effect.interact.{RecycleItemEffect, UnequipItemEffect}

case class RecycleItemAction(target: ItemId) extends Action {
  def apply(s: State, actor: CreatureId): List[Effect] = {
    val equipped = s.creature.equipments.get(actor) exists (_.values exists (_ == this.target))

    if (equipped) {
      List(
        TickEffect(actor),
        UnequipItemEffect(actor, this.target, actor(s).location),
        RecycleItemEffect(actor, this.target)
      )
    } else {
      List(
        TickEffect(actor),
        RecycleItemEffect(actor, this.target)
      )
    }
  }
}
