package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import io.github.fiifoo.scarl.core.mutation.{ItemContainerMutation, RemovableEntityMutation}

case class PickItemEffect(target: ItemId,
                          picker: CreatureId,
                          parent: Option[Effect] = None
                         ) extends Effect {

  def apply(s: State): EffectResult = {
    EffectResult(List(
      ItemContainerMutation(target, picker),
      RemovableEntityMutation(target(s).container)
    ))
  }
}
