package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.Time.Tick
import io.github.fiifoo.scarl.core.creature.Stats
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.Selectors.getCreatureStats
import io.github.fiifoo.scarl.core.entity.{ActorId, Creature, CreatureId}
import io.github.fiifoo.scarl.core.mutation.ActorTickMutation
import io.github.fiifoo.scarl.core.{State, Time}
import io.github.fiifoo.scarl.effect.creature.{RegenerateEffect, ScanEffect}

case class TickEffect(target: ActorId,
                      amount: Tick = Time.turn,
                      parent: Option[Effect] = None
                     ) extends Effect {

  def apply(s: State): EffectResult = {
    val tickMutation = ActorTickMutation(target, target(s).tick + amount)
    val effects = target match {
      case creature: CreatureId => creatureEffects(s, creature(s))
      case _ => List()
    }

    EffectResult(tickMutation, effects)
  }

  private def creatureEffects(s: State, creature: Creature): List[Effect] = {
    val stats = getCreatureStats(s)(creature.id)

    List(
      scan(s, creature, stats),
      regenerate(s, creature, stats)
    ).flatten
  }

  private def scan(s: State, creature: Creature, stats: Stats): Option[Effect] = {
    if (stats.sight.sensors > 0) {
      Some(ScanEffect(creature.id, creature.location))
    } else {
      None
    }
  }

  private def regenerate(s: State, creature: Creature, stats: Stats): Option[Effect] = {
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
