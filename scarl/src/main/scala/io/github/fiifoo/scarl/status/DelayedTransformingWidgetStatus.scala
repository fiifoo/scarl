package io.github.fiifoo.scarl.status

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.Time.Tick
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.WidgetKindId
import io.github.fiifoo.scarl.effect.area.TransformEffect

case class DelayedTransformingWidgetStatus(id: ActiveStatusId,
                                           tick: Tick,
                                           target: ContainerId,
                                           transformTo: WidgetKindId,
                                           transformDescription: Option[String] = None
                                          ) extends ActiveStatus {

  def setTick(tick: Tick): DelayedTransformingWidgetStatus = copy(tick = tick)

  def apply(s: State): List[Effect] = {
    List(TransformEffect(
      from = target,
      to = transformTo,
      description = transformDescription
    ))
  }
}
