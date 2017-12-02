package io.github.fiifoo.scarl.core.effect

import io.github.fiifoo.scarl.core.Selectors.getCreatureStats
import io.github.fiifoo.scarl.core.Time.Tick
import io.github.fiifoo.scarl.core.entity.{ActorId, Creature, CreatureId}
import io.github.fiifoo.scarl.core.mutation.ActorTickMutation
import io.github.fiifoo.scarl.core.{State, Time}

case class TickEffect(target: ActorId,
                      amount: Tick = Time.turn,
                      parent: Option[Effect] = None
                     ) extends Effect {

  def apply(s: State): EffectResult = {
    val tickMutation = ActorTickMutation(target, target(s).tick + amount)
    val regenerateEffect = target match {
      case creature: CreatureId => regenerate(s, creature(s))
      case _ => None
    }

    regenerateEffect map (regenerateEffect => {
      EffectResult(tickMutation, regenerateEffect)
    }) getOrElse {
      EffectResult(tickMutation)
    }
  }

  private def regenerate(s: State, creature: Creature): Option[Effect] = {
    val stats = getCreatureStats(s)(creature.id)

    val health = if (stats.health.regen > 0 && creature.damage > 0) stats.health.regen else 0
    val energy = if (stats.energy.regen > 0 && creature.energy < stats.energy.max) stats.energy.regen else 0
    val materials = if (stats.materials.regen > 0 && creature.materials < stats.materials.max) stats.materials.regen else 0

    if (health > 0 || energy > 0 || materials > 0) {
      Some(RegenerateEffect(
        creature.id,
        calculateRegeneration(health),
        calculateRegeneration(energy),
        calculateRegeneration(materials),
        Some(this)),
      )
    } else {
      None
    }
  }

  private def calculateRegeneration(amount: Double): Double = {
    amount / Time.turn * this.amount
  }
}
