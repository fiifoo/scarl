package io.github.fiifoo.scarl.ai.intention

import io.github.fiifoo.scarl.ai.tactic.ChargeTactic
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Intention
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.entity.{CreatureId, SafeCreatureId}
import io.github.fiifoo.scarl.core.geometry.Los

import scala.util.Random

case class ChargeIntention(target: SafeCreatureId) extends Intention {

  def apply(s: State, actor: CreatureId, random: Random): Option[Result] = {
    target(s) flatMap (target => {
      val from = actor(s).location
      val to = target.location
      val range = actor(s).stats.sight.range

      if (Los.locations(s)(from, to, range)) {
        Utils.move(s, actor, target.location) map (action => {
          val tactic = ChargeTactic(SafeCreatureId(target.id), target.location)

          (tactic, action)
        })
      } else {
        None
      }
    })
  }
}
