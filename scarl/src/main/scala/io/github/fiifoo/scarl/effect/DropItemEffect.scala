package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{Container, ContainerId, ItemId}
import io.github.fiifoo.scarl.core.mutation.{ItemContainerMutation, NewEntityMutation}
import io.github.fiifoo.scarl.core.{Location, State}

case class DropItemEffect(target: ItemId,
                          location: Location,
                          parent: Option[Effect] = None
                         ) extends Effect {

  def apply(s: State): EffectResult = {
    val container = Container(ContainerId(s.nextEntityId), location)

    EffectResult(List(
      NewEntityMutation(container),
      ItemContainerMutation(target, container.id)
    ))
  }
}
