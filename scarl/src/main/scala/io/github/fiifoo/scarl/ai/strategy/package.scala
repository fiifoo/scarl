package io.github.fiifoo.scarl.ai

import io.github.fiifoo.scarl.ai.intention.TravelIntention
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Brain.Intentions
import io.github.fiifoo.scarl.core.ai.Priority
import io.github.fiifoo.scarl.core.creature.FactionId
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.entity.Signal.{CreatureSignal, NoiseSignal}
import io.github.fiifoo.scarl.core.geometry.{Distance, Sector}
import io.github.fiifoo.scarl.rule.SignalRule

import scala.util.Random

package object strategy {

  def getMembers(s: State, faction: FactionId): Set[CreatureId] = {
    s.index.factionMembers.getOrElse(faction, Set())
  }

  def mergeIntentions(intentions: List[Intentions]): Intentions = {
    val initial: Intentions = Map()

    (intentions foldLeft initial) ((result, intentions) => {
      result ++ intentions map { case (k, v) => k -> (v ::: result.getOrElse(k, List())) }
    })
  }

  def calculateInvestigateSignals(s: State, faction: FactionId,
                                  members: Set[CreatureId],
                                  random: Random
                                 ): Intentions = {
    val signals = s.signals filter (signal => {
      (signal.owner forall (_ == faction)) && (signal.kind match {
        case CreatureSignal => true
        case NoiseSignal => true
        case _ => false
      })
    })

    val strengths = (signals foldLeft Map[Sector, Int]()) ((strengths, signal) => {
      val sector = Sector.apply(s)(signal.location)
      val strength = math.max(strengths.getOrElse(sector, 0), signal.strength)

      strengths + (sector -> strength)
    })

    // returns one intention per random detected signal
    strengths flatMap (x => {
      val (sector, strength) = x
      val destination = sector.center(s)

      val investigators = members filter (member => {
        val creature = member(s)
        val sensors = creature.stats.sight.sensors // equipment not used
        val distance = Distance(destination, creature.location)

        SignalRule.rollDetection(random)(
          SignalRule.calculateStrength(strength, sensors)(distance)
        )
      })

      (investigators map (_ -> List(
        (TravelIntention(destination), Priority.low)
      ))).toMap
    })
  }
}
