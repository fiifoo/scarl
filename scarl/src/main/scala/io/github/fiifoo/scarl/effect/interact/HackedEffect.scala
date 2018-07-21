package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, LockableId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.mutation.LockableLockedMutation

case class HackedEffect(hacker: CreatureId,
                        target: LockableId,
                        location: Location,
                        parent: Option[Effect] = None
                       ) extends Effect {

  def apply(s: State): EffectResult = {
    EffectResult(
      LockableLockedMutation(target, None)
    )
  }
}
