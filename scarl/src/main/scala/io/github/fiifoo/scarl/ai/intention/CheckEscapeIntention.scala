package io.github.fiifoo.scarl.ai.intention

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Intention
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.entity.Selectors.{getLocationWaypoint, getWaypointCreatures}
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId}
import io.github.fiifoo.scarl.core.geometry.WaypointNetwork.Waypoint
import io.github.fiifoo.scarl.core.geometry.{Location, WaypointPath}

import scala.util.Random

case object CheckEscapeIntention extends Intention {

  def apply(s: State, actor: CreatureId, random: Random): Option[Result] = {
    Utils.findVisibleEnemy(s, actor) flatMap escape(s, actor, random)
  }

  private def escape(s: State, actor: CreatureId, random: Random)(enemy: Creature): Option[Result] = {
    selectDestination(s, actor, enemy, random) flatMap (destination => {
      EscapeIntention(enemy.location, destination)(s, actor, random)
    })
  }

  private def selectDestination(s: State, actor: CreatureId, enemy: Creature, random: Random): Option[Location] = {
    getLocationWaypoint(s)(actor(s).location) flatMap (from => {
      val faction = actor(s).faction
      val exclude = getLocationWaypoint(s)(enemy.location) map (Set(_)) getOrElse Set()

      def valid(waypoint: Waypoint): Boolean = {
        (getWaypointCreatures(s)(waypoint) - actor) exists (_ (s).faction == faction)
      }

      WaypointPath.find(s)(from, valid, exclude) map (_.last)
    })
  }
}
