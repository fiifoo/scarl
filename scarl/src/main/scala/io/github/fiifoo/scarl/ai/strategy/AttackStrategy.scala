package io.github.fiifoo.scarl.ai.strategy

import io.github.fiifoo.scarl.ai.intention.{ScoutIntention, TravelIntention}
import io.github.fiifoo.scarl.ai.tactic.{AttackTactic, PursueTactic, ScoutTactic}
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.{Brain, Priority, Strategy}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.entity.Selectors.{getCreatureWaypoint, getLocationWaypoint}
import io.github.fiifoo.scarl.core.geometry.WaypointNetwork.Waypoint

import scala.util.Random

case class AttackStrategy(assault: Set[Waypoint] = Set(),
                          scouted: Set[Waypoint] = Set(),
                          investigate: Set[Waypoint] = Set()
                         ) extends Strategy {

  def apply(s: State, brain: Brain, random: Random): Brain = {
    val members = getMembers(s, brain.faction)
    val waypoints = members flatMap getCreatureWaypoint(s)

    val assault = validWaypoints(s, this.assault) -- waypoints ++ getEnemyWaypoints(s, members)
    val scouted = validWaypoints(s, this.scouted) ++ waypoints
    val scout = s.cache.waypointNetwork.waypoints -- scouted
    val investigate = validWaypoints(s, this.investigate) -- waypoints ++ calculateInvestigateSignals(
      s,
      brain.faction,
      members,
      s.cache.waypointNetwork.waypoints -- waypoints,
      random
    )

    val mobile = members filterNot (_ (s).immobile)
    val (scouts, leaders) = mobile filter (x => x(s).party.leader == x) partition (_ (s).behavior.isInstanceOf[ScoutTactic])

    if (assault.isEmpty && scout.isEmpty) {
      brain.copy(strategy = RoamStrategy(investigate))
    } else {
      val intentions = mergeIntentions(List(
        calculateTravelIntentions(s, mobile, investigate, TravelIntention(_), Priority.low, maxDistance = Some(2)),
        calculateTravelIntentions(s, scouts, scout, l => ScoutIntention(Some(l)), Priority.low),
        if (assault.nonEmpty) {
          calculateTravelIntentions(s, leaders, assault, TravelIntention(_), Priority.low + 1, partyMovement = true)
        } else {
          calculateTravelIntentions(s, leaders, scout, TravelIntention(_), Priority.low, partyMovement = true)
        }
      ))

      brain.copy(
        strategy = AttackStrategy(assault, scouted, investigate),
        intentions = intentions
      )
    }
  }

  private def getEnemyWaypoints(s: State, members: Set[CreatureId]): Set[Waypoint] = {
    val locations = members flatMap s.creature.tactics.get collect {
      case tactic: AttackTactic => tactic.destination
      case tactic: PursueTactic => tactic.destination
    }

    locations flatMap getLocationWaypoint(s)
  }
}
