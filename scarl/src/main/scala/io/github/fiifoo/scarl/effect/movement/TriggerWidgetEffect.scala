package io.github.fiifoo.scarl.effect.movement

import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult, LocalizedDescriptionEffect}
import io.github.fiifoo.scarl.core.entity.{ContainerId, CreatureId, ItemId}
import io.github.fiifoo.scarl.core.item.{Discover, DiscoverEveryone, DiscoverTriggerer}
import io.github.fiifoo.scarl.core.mutation.{ItemFoundMutation, ItemHiddenMutation}
import io.github.fiifoo.scarl.core.{Location, Selectors, State}

case class TriggerWidgetEffect(triggerer: CreatureId,
                               trap: ContainerId,
                               location: Location,
                               discover: Option[Discover] = None,
                               description: Option[String] = None,
                               parent: Option[Effect] = None
                              ) extends Effect with LocalizedDescriptionEffect {

  def apply(s: State): EffectResult = {
    val discoverMutation = discover flatMap (discover => {
      getHiddenItem(s) map (item => {
        discover match {
          case DiscoverTriggerer => ItemFoundMutation(item, triggerer)
          case DiscoverEveryone => ItemHiddenMutation(item, hidden = false)
        }
      })
    })

    discoverMutation map (EffectResult(_)) getOrElse EffectResult()
  }

  private def getHiddenItem(s: State): Option[ItemId] = {
    Selectors.getContainerItems(s)(trap).headOption collect {
      case item: ItemId if item(s).hidden => item
    }
  }
}
