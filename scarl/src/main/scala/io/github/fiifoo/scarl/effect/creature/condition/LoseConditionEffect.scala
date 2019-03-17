package io.github.fiifoo.scarl.effect.creature.condition

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{ConditionStatus, CreatureId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.mutation._

case class LoseConditionEffect(source: ConditionStatus,
                               target: CreatureId,
                               location: Location,
                               parent: Option[Effect] = None
                              ) extends Effect {

  def apply(s: State): EffectResult = {
    EffectResult(
      RemovableEntityMutation(this.source.id)
    )
  }
}
