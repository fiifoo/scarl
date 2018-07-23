package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId, LockableId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item.Lock
import io.github.fiifoo.scarl.core.mutation.LockableLockedMutation
import io.github.fiifoo.scarl.rule.HackRule
import io.github.fiifoo.scarl.rule.HackRule.FailureResult

case class HackFailedEffect(hacker: CreatureId,
                            target: LockableId,
                            lock: Lock,
                            location: Location,
                            failure: FailureResult,
                            parent: Option[Effect] = None
                           ) extends Effect {

  def apply(s: State): EffectResult = {
    val mutations = HackRule.increaseSecurity(failure)(lock.security) map (security => {
      val modified = target(s).locked flatMap getModifiedLock(security)

      List(LockableLockedMutation(target, modified))
    }) getOrElse {
      List()
    }

    val effects = (target match {
      case item: ItemId => trap(s, item)
      case _ => None
    }) getOrElse List()

    EffectResult(mutations, effects)
  }

  private def getModifiedLock(security: Int)(lock: Lock): Option[Lock] = {
    if (lock == this.lock) {
      Some(lock.copy(
        security = security
      ))
    } else {
      lock.sub map (sub => {
        lock.copy(
          sub = getModifiedLock(security)(sub)
        )
      })
    }
  }

  private def trap(s: State, item: ItemId): Option[List[Effect]] = {
    item(s).trap flatMap (trap => {
      if (HackRule.shouldTriggerTrap(failure)) {
        Some(trap(s, item, Some(hacker)))
      } else {
        None
      }
    })
  }
}
