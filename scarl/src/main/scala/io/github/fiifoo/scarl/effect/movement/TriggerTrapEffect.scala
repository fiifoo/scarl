package io.github.fiifoo.scarl.effect.movement

import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult, LocalizedDescriptionEffect}
import io.github.fiifoo.scarl.core.entity.{ContainerId, CreatureId, ItemId}
import io.github.fiifoo.scarl.core.mutation.ItemHiddenMutation
import io.github.fiifoo.scarl.core.{Location, Selectors, State}

case class TriggerTrapEffect(triggerer: CreatureId,
                             widget: ContainerId,
                             location: Location,
                             description: Option[String],
                             parent: Option[Effect] = None
                            ) extends Effect with LocalizedDescriptionEffect {

  def apply(s: State): EffectResult = {
    val discover = Selectors.getContainerItems(s)(widget).headOption collect {
      case item: ItemId if item(s).hidden => ItemHiddenMutation(item, hidden = false)
    }

    discover map (EffectResult(_)) getOrElse EffectResult()
  }
}
