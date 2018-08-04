package io.github.fiifoo.scarl.status

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.item.Discover
import io.github.fiifoo.scarl.effect.movement.TriggerWidgetEffect

case class TriggeredTrapStatus(id: TriggerStatusId,
                               target: ContainerId,
                               smart: Boolean = false,
                               discover: Option[Discover] = None,
                               triggerDescription: Option[String] = None,
                              ) extends TriggerStatus {

  def apply(s: State, triggerer: CreatureId): List[Effect] = {
    if (smart && !Utils.isSmartWidgetEnemy(s, target)(triggerer)) {
      return List()
    }

    val effects = Selectors.getWidgetItem(s)(target) flatMap (item => {
      item(s).trap map (trap => {
        trap(s, item, Some(triggerer))
      })
    }) getOrElse List()

    TriggerWidgetEffect(triggerer, target, target(s).location, discover, triggerDescription) :: effects
  }
}
