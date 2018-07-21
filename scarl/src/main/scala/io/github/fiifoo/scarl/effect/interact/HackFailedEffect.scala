package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId, LockableId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.mutation.LockableLockedMutation
import io.github.fiifoo.scarl.rule.HackRule
import io.github.fiifoo.scarl.rule.HackRule.FailureResult

case class HackFailedEffect(hacker: CreatureId,
                            target: LockableId,
                            location: Location,
                            failure: FailureResult,
                            parent: Option[Effect] = None
                           ) extends Effect {

  def apply(s: State): EffectResult = {
    val mutations = target(s).locked flatMap HackRule.failureLock(failure) map (lock => {
      List(LockableLockedMutation(target, Some(lock)))
    }) getOrElse List()

    val effects = (target match {
      case item: ItemId => trap(s, item)
      case _ => None
    }) getOrElse List()

    EffectResult(mutations, effects)
  }

  private def trap(s: State, item: ItemId): Option[List[Effect]] = {
    item(s).trap flatMap (trap => {
      if (HackRule.failureTrap(failure)) {
        Some(trap(s, item, Some(hacker)))
      } else {
        None
      }
    })
  }
}
