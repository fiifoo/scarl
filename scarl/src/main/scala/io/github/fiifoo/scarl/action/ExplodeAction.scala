package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.{Effect, TickEffect}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.effect.ExplodeEffect

case object ExplodeAction extends Action {
  val cost = 100

  def apply(s: State, actor: CreatureId): List[Effect] = {
    val creature = actor(s)
    val location = creature.location
    val explosive = creature.stats.explosive

    List(
      TickEffect(actor, cost),
      ExplodeEffect(actor, location, explosive)
    )
  }
}
