package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.Selectors.hasKey
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import io.github.fiifoo.scarl.core.{Location, State}

case class UseItemEffect(user: CreatureId,
                         target: ItemId,
                         location: Location,
                         parent: Option[Effect] = None
                        ) extends Effect {

  def apply(s: State): EffectResult = {
    val item = target(s)

    item.usable map (power => {
      item.lock flatMap (lock => {
        if (hasKey(s)(user)(lock)) {
          None
        } else {
          Some(EffectResult(
            ItemLockedEffect(user, target, location, Some(this))
          ))
        }
      }) getOrElse {
        EffectResult(
          power(s, target, Some(user))
        )
      }
    }) getOrElse EffectResult()
  }
}
