package io.github.fiifoo.scarl.power

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Condition
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Power.Resources
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.effect.creature.condition.RollConditionsEffect

case class RollCreatureConditionPower(description: Option[String] = None,
                                      resources: Option[Resources] = None,
                                      condition: Condition,
                                     ) extends CreaturePower {
  def apply(s: State, creature: CreatureId, user: Option[CreatureId]): List[Effect] = {
    List(
      RollConditionsEffect(creature, List(this.condition), creature(s).location)
    )
  }
}
