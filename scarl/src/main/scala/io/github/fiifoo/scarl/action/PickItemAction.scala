package fi.fiifoo.scarl.action

import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.action.Action
import fi.fiifoo.scarl.core.effect.Effect
import fi.fiifoo.scarl.core.entity.{Creature, Item}
import fi.fiifoo.scarl.effect.{PickItemEffect, TickEffect}

case class PickItemAction(item: Item) extends Action {
  val cost = 200

  def apply(s: State, actor: Creature): List[Effect] = {
    List(
      TickEffect(actor.id, cost),
      PickItemEffect(item.id, actor.id)
    )
  }
}
