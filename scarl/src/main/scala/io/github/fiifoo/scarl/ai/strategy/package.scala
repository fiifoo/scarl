package io.github.fiifoo.scarl.ai

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Brain.Intentions
import io.github.fiifoo.scarl.core.ai.{Intention, Priority}
import io.github.fiifoo.scarl.core.creature.FactionId
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.entity.Selectors.{getCreaturePartyMembers, getCreatureWaypoint}
import io.github.fiifoo.scarl.core.entity.Signal.{CreatureSignal, NoiseSignal}
import io.github.fiifoo.scarl.core.geometry.WaypointNetwork.Waypoint
import io.github.fiifoo.scarl.core.geometry.{Distance, Location, WaypointPath}
import io.github.fiifoo.scarl.rule.SignalRule

import scala.util.Random

package object strategy {

  def getMembers(s: State, faction: FactionId): Set[CreatureId] = {
    s.index.factionMembers.getOrElse(faction, Set())
  }

  def validWaypoints(s: State, waypoints: Set[Waypoint]): Set[Waypoint] = {
    waypoints intersect s.cache.waypointNetwork.waypoints
  }

  def mergeIntentions(intentions: List[Intentions]): Intentions = {
    val initial: Intentions = Map()

    (intentions foldLeft initial) ((result, intentions) => {
      result ++ intentions map { case (k, v) => k -> (v ::: result.getOrElse(k, List())) }
    })
  }

  def calculateTravelIntentions(s: State,
                                creatures: Set[CreatureId],
                                waypoints: Set[Waypoint],
                                getIntention: Location => Intention,
                                priority: Priority.Value,
                                partyMovement: Boolean = false,
                                maxDistance: Option[Int] = None,
                               ): Intentions = {
    (creatures flatMap (creature => {
      getCreatureWaypoint(s)(creature) flatMap (from => {
        WaypointPath.find(s)(from, waypoints.contains, maxDistance = maxDistance) map (_.last) map (waypoint => {
          val intention = getIntention(waypoint)
          val party = if (partyMovement) {
            getCreaturePartyMembers(s)(creature) filterNot (_ (s).immobile)
          } else {
            Set(creature)
          }

          party map (_ -> List((intention, priority)))
        })
      }) getOrElse Set()
    })).toMap
  }

  def calculateInvestigateSignals(s: State,
                                  faction: FactionId,
                                  members: Set[CreatureId],
                                  valid: Set[Waypoint],
                                  random: Random
                                 ): Set[Waypoint] = {
    def detected(waypoint: Waypoint, strength: Int): Boolean = {
      members exists (member => {
        val creature = member(s)
        val sensors = creature.stats.sight.sensors // equipment not used
        val distance = Distance(waypoint, creature.location)

        SignalRule.rollDetection(random)(
          SignalRule.calculateStrength(strength, sensors)(distance)
        )
      })
    }

    (calculateWaypointSignals(s, faction) collect {
      case (waypoint, strength) if valid.contains(waypoint) && detected(waypoint, strength) =>
        (waypoint.adjacent intersect valid) + waypoint
    }).flatten.toSet
  }

  private def calculateWaypointSignals(s: State, faction: FactionId): Map[Waypoint, Int] = {
    val signals = s.signals filter (signal => {
      (signal.owner forall (_ == faction)) && (signal.kind match {
        case CreatureSignal => true
        case NoiseSignal => true
        case _ => false
      })
    })

    (signals foldLeft Map[Waypoint, Int]()) ((result, signal) => {
      s.cache.waypointNetwork.locationWaypoint.get(signal.location) map (waypoint => {
        val strength = math.max(result.getOrElse(waypoint, 0), signal.strength)

        result + (waypoint -> strength)
      }) getOrElse {
        result
      }
    })
  }
}
