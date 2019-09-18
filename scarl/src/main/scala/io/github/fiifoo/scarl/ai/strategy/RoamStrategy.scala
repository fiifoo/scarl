package io.github.fiifoo.scarl.ai.strategy

import io.github.fiifoo.scarl.ai.intention.TravelIntention
import io.github.fiifoo.scarl.ai.tactic.EscapeTactic
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Brain.Intentions
import io.github.fiifoo.scarl.core.ai.{Brain, Priority, Strategy}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.entity.Selectors.{getCreaturePartyMembers, getCreatureWaypoint, getWaypointCreatures}
import io.github.fiifoo.scarl.core.geometry.WaypointNetwork.Waypoint

import scala.util.Random

case class RoamStrategy(investigate: Set[Waypoint] = Set()) extends Strategy {

  def apply(s: State, brain: Brain, random: Random): Brain = {
    val members = getMembers(s, brain.faction)
    val waypoints = members flatMap getCreatureWaypoint(s)

    val investigate = validWaypoints(s, this.investigate) -- waypoints ++ calculateInvestigateSignals(
      s,
      brain.faction,
      members,
      s.cache.waypointNetwork.waypoints -- waypoints,
      random
    )

    val mobile = members filterNot (_ (s).immobile)

    brain.copy(
      strategy = RoamStrategy(investigate),
      intentions = mergeIntentions(List(
        calculateTravelIntentions(s, mobile, investigate, TravelIntention(_), Priority.low, maxDistance = Some(1)),
        calculateEscapingIntentions(s, members)
      )))
  }

  private def calculateEscapingIntentions(s: State, members: Set[CreatureId]): Intentions = {
    val escaping = members filter (s.creature.tactics.get(_) exists (_.isInstanceOf[EscapeTactic]))

    val destinations = (escaping.toList flatMap (creature => {
      val allies = getCreatureWaypoint(s)(creature) flatMap getWaypointAllies(s, creature)

      allies map (allies => {
        val location = s.creature.tactics(creature).asInstanceOf[EscapeTactic].source
        val creatures = allies + creature

        (creatures map (_ -> location)).toMap
      }) getOrElse Map()
    })).toMap

    destinations transform ((_, destination) => {
      List((TravelIntention(destination), Priority.low))
    })
  }

  private def getWaypointAllies(s: State, creature: CreatureId)(waypoint: Waypoint): Option[Set[CreatureId]] = {
    val faction = creature(s).faction
    val allies = (getWaypointCreatures(s)(waypoint) - creature) filter (_ (s).faction == faction)

    if (allies.nonEmpty) {
      Some(allies flatMap getCreaturePartyMembers(s))
    } else {
      None
    }
  }
}
