package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.Selectors.getContainerItems
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{ContainerId, CreatureId, ItemId}
import io.github.fiifoo.scarl.core.mutation.{ItemContainerMutation, RemovableEntityMutation}
import io.github.fiifoo.scarl.core.{Location, State}

case class PickItemEffect(target: ItemId,
                          picker: CreatureId,
                          location: Location,
                          parent: Option[Effect] = None
                         ) extends Effect {

  def apply(s: State): EffectResult = {

    val removeContainer = target(s).container match {
      case container: ContainerId if getContainerItems(s)(container).size == 1 =>
        Some(RemovableEntityMutation(target(s).container))
      case _ => None
    }

    EffectResult(List(
      Some(ItemContainerMutation(target, picker)),
      removeContainer
    ).flatten)
  }
}
