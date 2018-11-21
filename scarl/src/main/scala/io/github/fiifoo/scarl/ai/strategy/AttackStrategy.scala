package io.github.fiifoo.scarl.ai.strategy

import io.github.fiifoo.scarl.ai.intention.{ScoutIntention, TravelIntention}
import io.github.fiifoo.scarl.ai.tactic.{AttackTactic, PursueTactic, ScoutTactic}
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Brain.Intentions
import io.github.fiifoo.scarl.core.ai.{Brain, Priority, Strategy}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.entity.Selectors.{getCreaturePartyMembers, getCreatureWaypoint, getLocationWaypoint}
import io.github.fiifoo.scarl.core.geometry.WaypointNetwork.Waypoint
import io.github.fiifoo.scarl.core.geometry.WaypointPath

import scala.util.Random

case class AttackStrategy(assault: Set[Waypoint] = Set(),
                          scouted: Set[Waypoint] = Set()
                         ) extends Strategy {

  def apply(s: State, brain: Brain, random: Random): Brain = {
    val members = getMembers(s, brain.faction)
    val waypoints = members flatMap getCreatureWaypoint(s)

    val assaultWaypoints = this.assault -- waypoints ++ getEnemyWaypoints(s, members)
    val scoutedWaypoints = this.scouted ++ waypoints
    val scoutWaypoints = s.cache.waypointNetwork.waypoints -- scoutedWaypoints

    if (assaultWaypoints.isEmpty && scoutWaypoints.isEmpty) {
      brain.copy(strategy = RoamStrategy)
    } else {
      brain.copy(
        strategy = AttackStrategy(assaultWaypoints, scoutedWaypoints),
        intentions = mergeIntentions(List(
          calculateInvestigateSignals(s, brain.faction, members, random),
          calculateIntentions(s, assaultWaypoints, scoutWaypoints, members)
        ))
      )
    }
  }

  private def calculateIntentions(s: State,
                                  assaultWaypoints: Set[Waypoint],
                                  scoutWaypoints: Set[Waypoint],
                                  members: Set[CreatureId]
                                 ): Intentions = {
    val (scouts, leaders) = members
      .filter(x => x(s).party.leader == x && !x(s).immobile)
      .partition(_ (s).behavior.isInstanceOf[ScoutTactic])

    calculateScoutIntentions(s, scoutWaypoints, scouts) ++
      calculateFighterIntentions(s, assaultWaypoints, scoutWaypoints, leaders)
  }

  private def calculateScoutIntentions(s: State,
                                       waypoints: Set[Waypoint],
                                       scouts: Set[CreatureId]
                                      ): Intentions = {
    val initial: (Intentions, Set[Waypoint]) = (Map(), waypoints)

    val (intentions, _) = (scouts foldLeft initial) ((carry, scout) => {
      val (intentions, waypoints) = carry

      (if (waypoints.isEmpty) {
        None
      } else {
        getCreatureWaypoint(s)(scout) flatMap (from => {
          WaypointPath.find(s)(from, waypoints.contains) map (_.last) map (waypoint => {
            val intention = (ScoutIntention(Some(waypoint)), Priority.medium)

            (intentions + (scout -> List(intention)), waypoints - waypoint)
          })
        })
      }) getOrElse carry
    })

    intentions
  }

  private def calculateFighterIntentions(s: State,
                                         assaultWaypoints: Set[Waypoint],
                                         scoutWaypoints: Set[Waypoint],
                                         leaders: Set[CreatureId]
                                        ): Intentions = {
    val waypoints = if (assaultWaypoints.isEmpty) scoutWaypoints else assaultWaypoints

    (leaders flatMap (leader => {
      getCreatureWaypoint(s)(leader) flatMap (from => {
        WaypointPath.find(s)(from, waypoints.contains) map (_.last) map (waypoint => {
          val party = getCreaturePartyMembers(s)(leader) filterNot (_ (s).immobile)
          val intention = (TravelIntention(waypoint), Priority.medium)

          party map (_ -> List(intention))
        })
      }) getOrElse Set()
    })).toMap
  }

  private def getEnemyWaypoints(s: State, members: Set[CreatureId]): Set[Waypoint] = {
    val locations = members flatMap s.tactics.get collect {
      case tactic: AttackTactic => tactic.destination
      case tactic: PursueTactic => tactic.destination
    }

    locations flatMap getLocationWaypoint(s)
  }
}
