package fi.fiifoo.scarl.effect

import fi.fiifoo.scarl.core.effect.{Effect, EffectResult}
import fi.fiifoo.scarl.core.entity.{Container, ContainerId, ItemId}
import fi.fiifoo.scarl.core.mutation.{ItemContainerMutation, NewEntityMutation}
import fi.fiifoo.scarl.core.{Location, State}

case class DropItemEffect(target: ItemId, location: Location) extends Effect {

  def apply(s: State): EffectResult = {
    val container = Container(ContainerId(s.nextEntityId), location)

    EffectResult(List(
      NewEntityMutation(container),
      ItemContainerMutation(target, container.id)
    ))
  }
}
