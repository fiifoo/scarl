package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, LockableId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item.Lock
import io.github.fiifoo.scarl.core.mutation.LockableLockedMutation

case class HackedEffect(hacker: CreatureId,
                        target: LockableId,
                        lock: Lock,
                        location: Location,
                        parent: Option[Effect] = None
                       ) extends Effect {

  def apply(s: State): EffectResult = {
    val next = target(s).locked flatMap getNextLock

    EffectResult(
      LockableLockedMutation(target, next)
    )
  }

  private def getNextLock(lock: Lock): Option[Lock] = {
    if (lock == this.lock) {
      lock.sub
    } else {
      lock.sub map (sub => {
        lock.copy(
          sub = getNextLock(sub)
        )
      })
    }
  }
}
