package fi.fiifoo.scarl.action

import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.action.Action
import fi.fiifoo.scarl.core.effect.Effect
import fi.fiifoo.scarl.core.entity.{Creature, Item}
import fi.fiifoo.scarl.effect.{DropItemEffect, TickEffect}

case class DropItemAction(item: Item) extends Action {
  val cost = 100

  def apply(s: State, actor: Creature): List[Effect] = {
    List(
      TickEffect(actor.id, cost),
      DropItemEffect(item.id, actor.location)
    )
  }

}
