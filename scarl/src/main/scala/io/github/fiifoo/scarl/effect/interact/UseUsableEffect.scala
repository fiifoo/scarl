package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.Selectors.hasLockKey
import io.github.fiifoo.scarl.core.entity.{CreatureId, CreaturePower, ItemPower, UsableId}
import io.github.fiifoo.scarl.core.geometry.Location

case class UseUsableEffect(user: CreatureId,
                           target: UsableId,
                           location: Location,
                           parent: Option[Effect] = None
                          ) extends Effect {

  def apply(s: State): EffectResult = {
    val usable = target(s)

    usable.usable map (power => {
      usable.locked flatMap (lock => {
        if (hasLockKey(s)(user)(lock)) {
          None
        } else {
          Some(EffectResult(
            LockedUsableEffect(user, target, location, Some(this))
          ))
        }
      }) getOrElse {

        val effects = power match {
          case power: CreaturePower => power(s, target, Some(user))
          case power: ItemPower => power(s, target, Some(user))
        }

        EffectResult(effects)
      }
    }) getOrElse EffectResult()
  }
}
