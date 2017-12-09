package io.github.fiifoo.scarl.effect.creature

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId}
import io.github.fiifoo.scarl.core.mutation.{CreatureEnergyMutation, CreatureMaterialsMutation, Mutation}

case class ConsumeEffect(target: CreatureId,
                         energy: Double,
                         materials: Double,
                         parent: Option[Effect] = None
                        ) extends Effect {

  def apply(s: State): EffectResult = {
    val creature = target(s)

    EffectResult(List(
      energyMutation(creature),
      materialsMutation(creature)
    ).flatten)
  }

  private def energyMutation(creature: Creature): Option[Mutation] = {
    if (energy > 0) {
      Some(CreatureEnergyMutation(target, (creature.energy - energy) max 0))
    } else {
      None
    }
  }

  private def materialsMutation(creature: Creature): Option[Mutation] = {
    if (materials > 0) {
      Some(CreatureMaterialsMutation(target, (creature.materials - materials) max 0))
    } else {
      None
    }
  }
}
