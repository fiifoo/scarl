package io.github.fiifoo.scarl.effect.area

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{CreateEntityEffect, Effect, EffectResult}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.core.math.Rng.WeightedChoices
import io.github.fiifoo.scarl.core.mutation.RngMutation

case class SummonCreatureEffect(choices: WeightedChoices[CreatureKindId],
                                location: Location,
                                description: Option[String] = None,
                                parent: Option[Effect] = None
                               ) extends Effect {

  def apply(s: State): EffectResult = {
    val (kind, rng) = s.rng.nextChoice(choices)

    EffectResult(
      RngMutation(rng),
      CreateEntityEffect(
        kind = kind,
        location = location,
        description = description,
        parent = Some(this)
      )
    )
  }
}
