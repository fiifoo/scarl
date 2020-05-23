package io.github.fiifoo.scarl.power

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Power.Resources
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.effect.combat.DeathEffect

case class DeathPower(description: Option[String] = None,
                      resources: Option[Resources] = None,
                     ) extends CreaturePower {

  def apply(s: State, creature: CreatureId, user: Option[CreatureId]): List[Effect] = {
    List(
      DeathEffect(creature, creature(s).location)
    )
  }
}
