package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{ContainerId, CreatureId, ItemId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.mutation.{ItemContainerMutation, RemovableEntityMutation}
import io.github.fiifoo.scarl.effect.creature.ReceiveKeyEffect

case class PickItemEffect(target: ItemId,
                          picker: CreatureId,
                          location: Location,
                          parent: Option[Effect] = None
                         ) extends Effect {

  def apply(s: State): EffectResult = {
    val item = target(s)

    val removeContainer = item.container match {
      case container: ContainerId => Some(RemovableEntityMutation(container))
      case _ => None
    }

    val receiveKey = item.key map (ReceiveKeyEffect(picker, _))

    EffectResult(
      List(
        Some(ItemContainerMutation(target, picker)),
        removeContainer
      ).flatten,
      List(
        receiveKey
      ).flatten
    )
  }
}
