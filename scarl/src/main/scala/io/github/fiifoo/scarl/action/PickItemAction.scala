package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.{Effect, TickEffect}
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import io.github.fiifoo.scarl.effect.PickItemEffect

case class PickItemAction(item: ItemId) extends Action {
  val cost = 100

  def apply(s: State, actor: CreatureId): List[Effect] = {
    List(
      TickEffect(actor, cost),
      PickItemEffect(item, actor)
    )
  }
}
