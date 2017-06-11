package io.github.fiifoo.scarl.status

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.WidgetKindId
import io.github.fiifoo.scarl.effect.TransformEffect

case class TriggeredTransformingWidgetStatus(id: TriggerStatusId,
                                             target: ContainerId,
                                             transformTo: WidgetKindId,
                                             transformDescription: Option[String] = None
                                            ) extends TriggerStatus {

  def apply(s: State, triggerer: CreatureId): List[Effect] = {
    List(TransformEffect(
      from = target,
      to = transformTo,
      description = transformDescription
    ))
  }
}
