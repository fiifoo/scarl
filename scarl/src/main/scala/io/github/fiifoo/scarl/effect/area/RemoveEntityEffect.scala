package io.github.fiifoo.scarl.effect.area

import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{ContainerId, EntityId, ItemId}
import io.github.fiifoo.scarl.core.mutation.RemovableEntityMutation
import io.github.fiifoo.scarl.core.{Location, State}

case class RemoveEntityEffect(target: EntityId,
                              location: Option[Location] = None,
                              description: Option[String] = None,
                              parent: Option[Effect] = None
                             ) extends Effect {

  def apply(s: State): EffectResult = {
    val remove = target match {
      case item: ItemId => item(s).container match {
        case container: ContainerId => container
        case _ => target
      }
      case _ => target
    }

    EffectResult(RemovableEntityMutation(remove))
  }
}
