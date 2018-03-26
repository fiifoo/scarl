package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.mutation.ItemLockedMutation
import io.github.fiifoo.scarl.rule.HackRule
import io.github.fiifoo.scarl.rule.HackRule.FailureResult

case class ItemHackFailedEffect(hacker: CreatureId,
                                item: ItemId,
                                location: Location,
                                failure: FailureResult,
                                parent: Option[Effect] = None
                               ) extends Effect {

  def apply(s: State): EffectResult = {
    val mutations = item(s).locked flatMap HackRule.failureLock(failure) map (lock => {
      List(ItemLockedMutation(item, Some(lock)))
    }) getOrElse List()

    val effects = item(s).trap flatMap (trap => {
      if (HackRule.failureTrap(failure)) {
        Some(trap(s, item, Some(hacker)))
      } else {
        None
      }
    }) getOrElse List()

    EffectResult(mutations, effects)
  }
}
