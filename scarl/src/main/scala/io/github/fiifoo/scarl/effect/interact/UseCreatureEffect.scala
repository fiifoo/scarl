package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.geometry.Location

case class UseCreatureEffect(user: CreatureId,
                             target: CreatureId,
                             location: Location,
                             parent: Option[Effect] = None
                            ) extends Effect {

  def apply(s: State): EffectResult = {
    target(s).usable map (power => {
      EffectResult(
        power(s, target, Some(user))
      )
    }) getOrElse EffectResult()
  }
}
