package io.github.fiifoo.scarl.power

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Power.Resources
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.effect.MaybeEffect
import io.github.fiifoo.scarl.effect.interact.PowerUseEffect

case class MaybeCreaturePower(description: Option[String] = None,
                              resources: Option[Resources] = None,
                              power: CreaturePower,
                              probability: Int,
                             ) extends CreaturePower {
  def apply(s: State, creature: CreatureId, user: Option[CreatureId]): List[Effect] = {
    List(MaybeEffect(PowerUseEffect(user, creature, this.power, requireResources = false), this.probability))
  }
}
