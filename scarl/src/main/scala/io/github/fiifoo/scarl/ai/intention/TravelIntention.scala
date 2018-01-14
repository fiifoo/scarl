package io.github.fiifoo.scarl.ai.intention

import io.github.fiifoo.scarl.ai.tactic.TravelTactic
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Intention
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.geometry.WaypointNetwork.Waypoint

import scala.util.Random

// todo: waypoint path finding
case class TravelIntention(destination: Waypoint) extends Intention {

  def apply(s: State, actor: CreatureId, random: Random): Option[Result] = {
    if (actor(s).location != destination) {
      Utils.move(s, actor, destination) map ((TravelTactic(destination), _))
    } else {
      None
    }
  }
}
