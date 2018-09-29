package io.github.fiifoo.scarl.ai.intention

import io.github.fiifoo.scarl.ai.tactic.ScoutTactic
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Intention
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.math.Rng

import scala.util.Random

case class ScoutIntention(destination: Option[Location] = None, waiting: Boolean = false) extends Intention {

  def apply(s: State, actor: CreatureId, random: Random): Option[Result] = {
    destination orElse selectDestination(s: State, actor: CreatureId, random: Random) flatMap (destination => {
      if (actor(s).location != destination) {
        Utils.travel(s, actor, destination, displace = true, !waiting) map (action => {
          (ScoutTactic(Some(destination), Utils.waited(action)), action)
        })
      } else {
        None
      }
    })
  }

  private def selectDestination(s: State, actor: CreatureId, random: Random): Option[Location] = {
    val choices = s.cache.waypointNetwork.waypoints - actor(s).location

    if (choices.isEmpty) {
      None
    } else {
      Some(Rng.nextChoice(random, choices))
    }
  }
}
