package io.github.fiifoo.scarl.effect.creature

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.item.Key
import io.github.fiifoo.scarl.core.mutation.CreatureKeyMutation

case class ReceiveKeyEffect(target: CreatureId,
                            key: Key,
                            parent: Option[Effect] = None
                           ) extends Effect {

  def apply(s: State): EffectResult = {
    EffectResult(CreatureKeyMutation(target, key))
  }
}
