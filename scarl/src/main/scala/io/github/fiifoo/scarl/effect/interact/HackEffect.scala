package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.Selectors.{getCreatureKeys, getCreatureStats}
import io.github.fiifoo.scarl.core.entity.{CreatureId, LockableId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item.Lock
import io.github.fiifoo.scarl.core.mutation.RngMutation
import io.github.fiifoo.scarl.rule.HackRule
import io.github.fiifoo.scarl.rule.HackRule.{FailureResult, Success}

case class HackEffect(hacker: CreatureId,
                      target: LockableId,
                      location: Location,
                      parent: Option[Effect] = None
                     ) extends Effect {

  def apply(s: State): EffectResult = {
    getLock(s) map (lock => {
      val (random, rng) = s.rng()
      val skill = getCreatureStats(s)(hacker).skill.hacking

      val effect = HackRule(random)(skill, lock.security) match {
        case Success => HackedEffect(hacker, target, lock, location, Some(this))
        case failure: FailureResult => HackFailedEffect(hacker, target, lock, location, failure, Some(this))
      }

      EffectResult(
        RngMutation(rng),
        effect
      )
    }) getOrElse EffectResult()
  }

  private def getLock(s: State): Option[Lock] = {
    val keys = getCreatureKeys(s)(hacker)

    def get(lock: Option[Lock]): Option[Lock] = {
      lock flatMap (lock => {
        if (lock.key exists keys.contains) {
          get(lock.sub)
        } else {
          Some(lock)
        }
      })
    }

    get(target(s).locked)
  }
}
