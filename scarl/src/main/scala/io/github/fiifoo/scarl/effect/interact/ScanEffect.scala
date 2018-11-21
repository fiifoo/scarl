package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.Selectors.getCreatureStats
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId, Signal}
import io.github.fiifoo.scarl.core.geometry.{Distance, Location}
import io.github.fiifoo.scarl.effect.area.SignalEffect
import io.github.fiifoo.scarl.rule.SignalRule

case class ScanEffect(scanner: CreatureId,
                      location: Location,
                      noise: Int,
                      parent: Option[Effect] = None
                     ) extends Effect {

  def apply(s: State): EffectResult = {
    val creature = scanner(s)
    val noiseSignal = SignalEffect(Signal.NoiseSignal, location, Signal.Weak)

    EffectResult(
      noiseSignal :: getCreatureSignals(s, creature) ::: getConduitSignals(s, creature)
    )
  }

  private def getCreatureSignals(s: State, scanner: Creature): List[Effect] = {
    val from = scanner.location
    val sensors = getCreatureStats(s)(scanner.id).sight.sensors
    val calculateFilterStrength = SignalRule.calculateStrength(Signal.Medium, sensors) _

    val creatures = (s.index.sectorCreatures filterKeys (sector => {
      val distance = Distance(from, sector.center(s))

      calculateFilterStrength(distance) > 0
    })).values.flatten.toSet

    (creatures - scanner.id).toList map (creature => {
      SignalEffect(
        Signal.CreatureSignal,
        creature(s).location,
        Signal.Weak,
        owner = Some(scanner.faction)
      )
    })
  }

  private def getConduitSignals(s: State, scanner: Creature): List[Effect] = {
    (s.conduits.entrances.values map (location => {
      SignalEffect(
        Signal.ConduitSignal,
        location,
        Signal.Strong,
        Signal.FuzzyRadius,
        owner = Some(scanner.faction)
      )
    })).toList
  }
}
