package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.{Location, Selectors, State}
import io.github.fiifoo.scarl.effect.{MoveEffect, TickEffect}

case class MoveAction(location: Location) extends Action {
  def apply(s: State, actor: CreatureId): List[Effect] = {
    val speed = Selectors.getCreatureStats(s)(actor).speed
    val cost = (100 / speed).round.toInt

    List(
      TickEffect(actor, cost),
      MoveEffect(actor, actor(s).location, location)
    )
  }
}
