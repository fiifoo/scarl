package fi.fiifoo.scarl.effect

import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.effect.{Effect, EffectResult}
import fi.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import fi.fiifoo.scarl.core.mutation.{ItemContainerMutation, RemovableEntityMutation}

case class PickItemEffect(target: ItemId, picker: CreatureId) extends Effect {

  def apply(s: State): EffectResult = {
    EffectResult(List(
      ItemContainerMutation(target, picker),
      RemovableEntityMutation(target(s).container)
    ))
  }
}
