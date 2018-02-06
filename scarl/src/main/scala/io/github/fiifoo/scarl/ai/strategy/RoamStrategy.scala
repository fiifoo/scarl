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

case object RoamStrategy extends Strategy {

  def apply(s: State, brain: Brain, random: Random): Brain = {
    val intentions = calculateEscaping(s, brain)

    brain.copy(intentions = intentions)
  }

  private def calculateEscaping(s: State, brain: Brain): Intentions = {
    val escaping = getMembers(s, brain) filter (s.tactics.get(_) exists (_.isInstanceOf[EscapeTactic]))

    val destinations = (escaping.toList flatMap (creature => {
      val allies = getCreatureWaypoint(s)(creature) flatMap getWaypointAllies(s, creature)

      allies map (allies => {
        val location = s.tactics(creature).asInstanceOf[EscapeTactic].source
        val creatures = allies + creature

        (creatures map (_ -> location)).toMap
      }) getOrElse Map()
    })).toMap

    destinations mapValues (destination => {
      List((TravelIntention(destination), Priority.medium))
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

  private def getMembers(s: State, brain: Brain): Set[CreatureId] = {
    s.index.factionMembers.getOrElse(brain.faction, Set())
  }

}
