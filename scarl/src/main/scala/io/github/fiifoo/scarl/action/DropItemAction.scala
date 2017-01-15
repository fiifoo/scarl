package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.{Creature, Item}
import io.github.fiifoo.scarl.effect.{DropItemEffect, TickEffect}

case class DropItemAction(item: Item) extends Action {
  val cost = 100

  def apply(s: State, actor: Creature): List[Effect] = {
    List(
      TickEffect(actor.id, cost),
      DropItemEffect(item.id, actor.location)
    )
  }

}
