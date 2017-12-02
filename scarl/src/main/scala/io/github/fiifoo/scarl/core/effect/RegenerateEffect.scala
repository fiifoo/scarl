package io.github.fiifoo.scarl.core.effect

import io.github.fiifoo.scarl.core.Selectors.getCreatureStats
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Stats
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId}
import io.github.fiifoo.scarl.core.mutation._

case class RegenerateEffect(target: CreatureId,
                            health: Double,
                            energy: Double,
                            materials: Double,
                            parent: Option[Effect] = None
                           ) extends Effect {

  def apply(s: State): EffectResult = {
    val creature = target(s)
    val stats = getCreatureStats(s)(target)

    EffectResult(List(
      damageMutation(creature),
      energyMutation(creature, stats),
      materialsMutation(creature, stats),
    ).flatten)
  }

  private def damageMutation(creature: Creature): Option[Mutation] = {
    if (health > 0) {
      Some(CreatureDamageMutation(target, (creature.damage - health) max 0))
    } else {
      None
    }
  }

  private def energyMutation(creature: Creature, stats: Stats): Option[Mutation] = {
    if (energy > 0) {
      Some(CreatureEnergyMutation(target, (creature.energy + energy) min stats.energy.max))
    } else {
      None
    }
  }

  private def materialsMutation(creature: Creature, stats: Stats): Option[Mutation] = {
    if (materials > 0) {
      Some(CreatureMaterialsMutation(target, (creature.materials + materials) min stats.materials.max))
    } else {
      None
    }
  }
}
