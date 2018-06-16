package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.Selectors.getCreatureStats
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.mutation.RngMutation
import io.github.fiifoo.scarl.rule.HackRule
import io.github.fiifoo.scarl.rule.HackRule.{FailureResult, Success}

case class HackItemEffect(hacker: CreatureId,
                          target: ItemId,
                          location: Location,
                          parent: Option[Effect] = None
                         ) extends Effect {

  def apply(s: State): EffectResult = {
    target(s).locked map (lock => {
      val (random, rng) = s.rng()
      val skill = getCreatureStats(s)(hacker).skill.hacking

      val effect = HackRule(random)(skill, lock.security) match {
        case Success => ItemHackedEffect(hacker, target, location, Some(this))
        case failure: FailureResult => ItemHackFailedEffect(hacker, target, location, failure, Some(this))
      }

      EffectResult(
        RngMutation(rng),
        effect
      )
    }) getOrElse EffectResult()
  }
}