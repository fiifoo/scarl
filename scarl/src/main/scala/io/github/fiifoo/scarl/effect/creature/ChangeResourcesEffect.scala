package io.github.fiifoo.scarl.effect.creature

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Stats
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.Selectors.getCreatureStats
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId}
import io.github.fiifoo.scarl.core.mutation._
import io.github.fiifoo.scarl.effect.combat.DamageEffect

case class ChangeResourcesEffect(target: CreatureId,
                                 health: Int,
                                 energy: Int,
                                 materials: Int,
                                 components: Int,
                                 parent: Option[Effect] = None
                                ) extends Effect {

  def apply(s: State): EffectResult = {
    val creature = target(s)
    val stats = getCreatureStats(s)(target)

    EffectResult(List(
      energyMutation(creature, stats),
      materialsMutation(creature, stats),
      componentsMutation(creature)
    ).flatten, List(
      healthEffect(s, creature)
    ).flatten)
  }

  private def healthEffect(s: State, creature: Creature): Option[Effect] = {
    if (health > 0) {
      Some(HealEffect(target, health, target(s).location, Some(this)))
    } else if (health < 0) {
      Some(DamageEffect(target, -health, Some(this)))
    } else {
      None
    }
  }

  private def energyMutation(creature: Creature, stats: Stats): Option[Mutation] = {
    if (energy != 0) {
      Some(CreatureEnergyMutation(target, (creature.resources.energy + energy) min stats.energy.max max 0))
    } else {
      None
    }
  }

  private def materialsMutation(creature: Creature, stats: Stats): Option[Mutation] = {
    if (materials != 0) {
      Some(CreatureMaterialsMutation(target, (creature.resources.materials + materials) min stats.materials.max max 0))
    } else {
      None
    }
  }

  private def componentsMutation(creature: Creature): Option[Mutation] = {
    if (components != 0) {
      Some(CreatureComponentsMutation(target, creature.resources.components + components))
    } else {
      None
    }
  }
}
