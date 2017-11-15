package io.github.fiifoo.scarl.effect.area

import io.github.fiifoo.scarl.core.Rng.WeightedChoices
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.core.mutation.RngMutation
import io.github.fiifoo.scarl.core.{Location, State}

case class SummonCreatureEffect(choices: WeightedChoices[CreatureKindId],
                                location: Location,
                                description: Option[String] = None,
                                parent: Option[Effect] = None
                               ) extends Effect {

  def apply(s: State): EffectResult = {
    val (kind, rng) = s.rng.nextChoice(choices)

    EffectResult(
      RngMutation(rng),
      CreateEntityEffect(kind, location, description, Some(this))
    )
  }
}
