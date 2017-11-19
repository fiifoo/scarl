package io.github.fiifoo.scarl.status

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.item.Discover
import io.github.fiifoo.scarl.effect.interact.ActivateMachineryEffect
import io.github.fiifoo.scarl.effect.movement.TriggerWidgetEffect

case class TriggeredMachineryStatus(id: TriggerStatusId,
                                    target: ContainerId,
                                    discover: Option[Discover] = None,
                                    description: Option[String] = None,
                                   ) extends TriggerStatus {

  def apply(s: State, triggerer: CreatureId): List[Effect] = {
    val location = target(s).location
    val machinery = s.index.locationMachinery.getOrElse(location, Set())

    if (machinery.isEmpty) {
      return List()
    }

    val triggerEffect = TriggerWidgetEffect(triggerer, target, target(s).location, discover, description)
    val machineryEffects = machinery.toList map (machinery => {
      ActivateMachineryEffect(triggerer, target(s).location, machinery)
    })

    triggerEffect :: machineryEffects
  }
}
