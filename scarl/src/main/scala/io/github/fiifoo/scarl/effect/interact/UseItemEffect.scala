package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import io.github.fiifoo.scarl.core.{Location, State}

case class UseItemEffect(user: CreatureId,
                         target: ItemId,
                         location: Location,
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