package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.Rng.WeightedChoices
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.LocatableId
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.core.mutation.RngMutation
import io.github.fiifoo.scarl.core.{Location, State}

case class SummonSomeCreatureEffect(choices: WeightedChoices[CreatureKindId],
                                    location: Location,
                                    source: LocatableId,
                                    parent: Option[Effect] = None
                                   ) extends Effect {

  def apply(s: State): EffectResult = {
    val (kind, rng) = s.rng.nextChoice(choices)
    val creature = kind(s)(s, location)

    EffectResult(
      RngMutation(rng),
      SummonCreatureEffect(creature, source)
    )
  }
}
