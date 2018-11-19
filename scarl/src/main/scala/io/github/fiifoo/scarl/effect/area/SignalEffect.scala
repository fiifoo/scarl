package io.github.fiifoo.scarl.effect.area

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.FactionId
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.Signal
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.mutation.{NewSignalMutation, RngMutation}

case class SignalEffect(kind: Signal.Kind,
                        location: Location,
                        strength: Int,
                        radius: Int = 0,
                        owner: Option[FactionId] = None,
                        parent: Option[Effect] = None
                       ) extends Effect {

  def apply(s: State): EffectResult = {
    val (random, rng) = s.rng()
    val seed = random.nextInt()
    val signal = Signal(kind, location, strength, radius, owner, seed, s.tick)

    EffectResult(List(
      RngMutation(rng),
      NewSignalMutation(signal)
    ))
  }
}
