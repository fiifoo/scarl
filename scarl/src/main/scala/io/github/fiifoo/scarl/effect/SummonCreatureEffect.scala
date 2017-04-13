package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{Creature, LocatableId}
import io.github.fiifoo.scarl.core.mutation.NewEntityMutation

case class SummonCreatureEffect(creature: Creature,
                                source: LocatableId,
                                parent: Option[Effect] = None
                               ) extends Effect {

  def apply(s: State): EffectResult = {
    EffectResult(NewEntityMutation(creature))
  }
}
