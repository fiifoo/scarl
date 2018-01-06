package io.github.fiifoo.scarl.status

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.item.Discover
import io.github.fiifoo.scarl.effect.combat.ExplodeEffect
import io.github.fiifoo.scarl.effect.movement.TriggerWidgetEffect

case class TriggeredExplosiveStatus(id: TriggerStatusId,
                                    target: ContainerId,
                                    discover: Option[Discover] = None,
                                    triggerDescription: Option[String] = None,
                                   ) extends TriggerStatus {

  def apply(s: State, triggerer: CreatureId): List[Effect] = {
    val effect = Selectors.getWidgetItem(s)(target) flatMap (item => {
      item(s).explosive map (stats => {
        ExplodeEffect(
          explosive = target,
          location = target(s).location,
          stats = stats,
        )
      })
    })

    effect map (effect => {
      List(
        TriggerWidgetEffect(triggerer, target, target(s).location, discover, triggerDescription),
        effect,
      )
    }) getOrElse {
      List()
    }
  }
}
