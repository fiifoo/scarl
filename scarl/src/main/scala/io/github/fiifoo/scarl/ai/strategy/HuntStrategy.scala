package io.github.fiifoo.scarl.ai.strategy

import io.github.fiifoo.scarl.ai.intention.TravelIntention
import io.github.fiifoo.scarl.ai.tactic.ChargeTactic
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Brain.Intentions
import io.github.fiifoo.scarl.core.ai.{Brain, Priority, Strategy, Tactic}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.geometry.WaypointNetwork.Waypoint

import scala.util.Random

case object HuntStrategy extends Strategy {

  def apply(s: State, brain: Brain, random: Random): Brain = {
    brain.copy(
      intentions = calculateIntentions(s, brain)
    )
  }

  private def calculateIntentions(s: State, brain: Brain): Intentions = {
    val members = getMembers(s, brain)

    val beacon = (getTactics(s, members) collectFirst {
      case charge: ChargeTactic =>
        charge.target(s) map (_.location) flatMap getLocationWaypoint(s)
    }).flatten

    beacon map (beacon => {
      (members map (member => {
        member -> List((
          TravelIntention(beacon),
          Priority.low
        ))
      })).toMap
    }) getOrElse Map()
  }

  private def getTactics(s: State, members: Set[CreatureId]): Set[Tactic] = {
    members flatMap s.tactics.get
  }

  private def getMembers(s: State, brain: Brain): Set[CreatureId] = {
    s.index.factionMembers.getOrElse(brain.faction, Set())
  }

  private def getLocationWaypoint(s: State)(location: Location): Option[Waypoint] = {
    s.cache.waypointNetwork.locationWaypoint.get(location)
  }
}
