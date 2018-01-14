package io.github.fiifoo.scarl.ai.intention

import io.github.fiifoo.scarl.ai.tactic.PursueTactic
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Intention
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.entity.{CreatureId, SafeCreatureId}
import io.github.fiifoo.scarl.core.geometry.Location

import scala.util.Random

case class PursueIntention(target: SafeCreatureId, destination: Location) extends Intention {

  def apply(s: State, actor: CreatureId, random: Random): Option[Result] = {
    if (actor(s).location != destination) {
      Utils.move(s, actor, destination) map (action => {
        val tactic = PursueTactic(target, destination)

        (tactic, action)
      })
    } else {
      None
    }
  }
}
