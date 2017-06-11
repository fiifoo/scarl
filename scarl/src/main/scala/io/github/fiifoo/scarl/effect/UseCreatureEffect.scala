package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId

case class UseCreatureEffect(user: CreatureId,
                             target: CreatureId,
                             parent: Option[Effect] = None
                            ) extends Effect {

  def apply(s: State): EffectResult = {
    target(s).usable map (power => {
      EffectResult(
        power(s)(s, target, Some(user))
      )
    }) getOrElse EffectResult()
  }
}
