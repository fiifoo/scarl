package io.github.fiifoo.scarl.effect.movement

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult, LocalizedDescriptionEffect}
import io.github.fiifoo.scarl.core.entity.{ContainerId, CreatureId, ItemId, Selectors}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item.Discover
import io.github.fiifoo.scarl.core.mutation.{ItemFoundMutation, ItemHiddenMutation}

case class TriggerWidgetEffect(triggerer: CreatureId,
                               trap: ContainerId,
                               location: Location,
                               discover: Option[Discover] = None,
                               description: Option[String] = None,
                               parent: Option[Effect] = None
                              ) extends Effect with LocalizedDescriptionEffect {

  def apply(s: State): EffectResult = {
    val discoverMutation = discover flatMap (discover => {
      getHiddenItem(s) flatMap (item => {
        discover match {
          case Discover.Triggerer => Some(ItemFoundMutation(item, triggerer))
          case Discover.Everyone => Some(ItemHiddenMutation(item, hidden = false))
          case Discover.Nobody => None
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
