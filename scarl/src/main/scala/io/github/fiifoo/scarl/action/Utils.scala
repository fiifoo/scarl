package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Stance
import io.github.fiifoo.scarl.core.creature.Stats.{AttackStats, Consumption}
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Selectors.getCreatureStanceStatuses
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId}
import io.github.fiifoo.scarl.effect.TickEffect
import io.github.fiifoo.scarl.effect.creature.{ChangeStanceEffect, ConsumeEffect, ShortageEffect}

object Utils {

  object Attack {
    def apply(s: State, actor: CreatureId, stats: AttackStats, attackEffect: Effect): List[Effect] = {
      val creature = actor(s)
      val consumption = stats.consumption
      val stance = stats.stance

      val tick = TickEffect(actor)

      shortage(creature, consumption) map (List(tick, _)) getOrElse {
        List(
          changeStance(s, actor, stance),
          Some(tick),
          Some(attackEffect),
          consume(actor, consumption)
        ).flatten
      }
    }

    private def shortage(creature: Creature, consumption: Consumption): Option[Effect] = {
      if (consumption.nonEmpty &&
        (consumption.energy > creature.resources.energy ||
          consumption.materials > creature.resources.materials
          )
      ) {
        Some(ShortageEffect(
          creature.id,
          consumption.energy > creature.resources.energy,
          consumption.materials > creature.resources.materials
        ))
      } else {
        None
      }
    }

    private def consume(creature: CreatureId, consumption: Consumption): Option[Effect] = {
      if (consumption.nonEmpty) {
        Some(ConsumeEffect(creature, consumption.energy, consumption.materials))
      } else {
        None
      }
    }

    def changeStance(s: State, creature: CreatureId, stance: Option[Stance]): Option[Effect] = {
      stance flatMap (stance => {
        if (getCreatureStanceStatuses(s)(creature) exists (_.stance.key == stance.key)) {
          None
        } else {
          Some(ChangeStanceEffect(creature, stance))
        }
      })
    }
  }

}
