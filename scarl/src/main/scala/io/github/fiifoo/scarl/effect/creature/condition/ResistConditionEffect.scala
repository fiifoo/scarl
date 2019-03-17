package io.github.fiifoo.scarl.effect.creature.condition

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Condition
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.geometry.Location

case class ResistConditionEffect(target: CreatureId,
                                 condition: Condition,
                                 location: Location,
                                 parent: Option[Effect] = None
                                ) extends Effect {

  def apply(s: State): EffectResult = EffectResult()
}
