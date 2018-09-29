package io.github.fiifoo.scarl.ai.intention

import io.github.fiifoo.scarl.ai.tactic.TravelTactic
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Intention
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.geometry.Location

import scala.util.Random

case class TravelIntention(destination: Location, waiting: Boolean = false) extends Intention {

  def apply(s: State, actor: CreatureId, random: Random): Option[Result] = {
    if (actor(s).location != destination) {
      Utils.travel(s, actor, destination, wait = !waiting) map (action => {
        (TravelTactic(destination, Utils.waited(action)), action)
      })
    } else {
      None
    }
  }
}
