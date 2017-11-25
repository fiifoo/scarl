package io.github.fiifoo.scarl.simulation

import io.github.fiifoo.scarl.core.creature.FactionId
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.{RealityBubble, State}
import io.github.fiifoo.scarl.effect.combat.{ExplodeEffect, ExplosionEffect}

class ExplosionOutcomeListener(enemies: Set[FactionId]) extends SimulationListener[Outcome] {

  def apply(state: SimulationState[Outcome], data: RealityBubble.Result): SimulationState[Outcome] = {
    val s = data.state

    val explode = data.effects.collectFirst {
      case explode: ExplodeEffect if s.simulation.entities contains explode.explosive => explode
    }

    explode map (explode => {
      val explosions = data.effects collect {
        case explosion: ExplosionEffect if explosion.parent contains explode => explosion
      }
      val targets = explosions flatMap (_.targets(s))

      if (targets.exists(friendly(s))) {
        result(state, Outcome.Bad)
      } else if (targets.exists(enemy(s))) {
        result(state, Outcome.Good)
      } else {
        result(state, Outcome.Neutral)
      }
    }) getOrElse state
  }

  private def friendly(s: State)(target: CreatureId): Boolean = {
    !enemy(s)(target)
  }

  private def enemy(s: State)(target: CreatureId): Boolean = {
    enemies.contains(target(s).faction)
  }

  private def result(state: SimulationState[Outcome], outcome: Outcome): SimulationState[Outcome] = {
    state.copy(
      result = outcome,
      stopped = true,
    )
  }
}
