package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.core.creature.Stats.Consumption
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Creature
import io.github.fiifoo.scarl.effect.creature.{ConsumeEffect, ShortageEffect}

object Utils {
  def shortage(creature: Creature, consumption: Consumption): Option[Effect] = {
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

  def consume(creature: Creature, consumption: Consumption): Option[Effect] = {
    if (consumption.nonEmpty) {
      Some(ConsumeEffect(creature.id, consumption.energy, consumption.materials))
    } else {
      None
    }
  }
}
