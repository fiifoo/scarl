package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.{Effect, TickEffect}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.effect.movement.MoveEffect
import io.github.fiifoo.scarl.rule.MoveRule

case class MoveAction(location: Location) extends Action {
  def apply(s: State, actor: CreatureId): List[Effect] = {
    val cost = MoveRule.cost(s, actor)

    List(
      TickEffect(actor, cost),
      MoveEffect(actor, actor(s).location, location)
    )
  }
}
