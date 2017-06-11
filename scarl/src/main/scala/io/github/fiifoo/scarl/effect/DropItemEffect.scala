package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{Container, ContainerId, CreatureId, ItemId}
import io.github.fiifoo.scarl.core.mutation.{ItemContainerMutation, NewEntityMutation}

case class DropItemEffect(target: ItemId,
                          dropper: CreatureId,
                          parent: Option[Effect] = None
                         ) extends Effect {

  def apply(s: State): EffectResult = {
    val container = Container(ContainerId(s.nextEntityId), dropper(s).location)

    EffectResult(List(
      NewEntityMutation(container),
      ItemContainerMutation(target, container.id)
    ))
  }
}
