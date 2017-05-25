package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{ContainerId, CreatureId, ItemId}
import io.github.fiifoo.scarl.core.mutation.ItemHiddenMutation
import io.github.fiifoo.scarl.core.{Selectors, State}

case class TriggerTrapEffect(triggerer: CreatureId,
                             widget: ContainerId,
                             description: String,
                             parent: Option[Effect] = None
                            ) extends Effect {

  def apply(s: State): EffectResult = {
    val discoverMutation = Selectors.getContainerItems(s)(widget).headOption collect {
      case item: ItemId if item(s).hidden => ItemHiddenMutation(item, hidden = false)
    }

    discoverMutation map (EffectResult(_)) getOrElse EffectResult()
  }
}
