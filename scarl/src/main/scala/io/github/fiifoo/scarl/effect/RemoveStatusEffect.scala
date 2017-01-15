package fi.fiifoo.scarl.effect

import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.effect.{Effect, EffectResult}
import fi.fiifoo.scarl.core.entity.StatusId
import fi.fiifoo.scarl.core.mutation.RemovableEntityMutation

case class RemoveStatusEffect(target: StatusId) extends Effect {

  def apply(s: State): EffectResult = {
    EffectResult(RemovableEntityMutation(target))
  }
}
