package io.github.fiifoo.scarl.status

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.WidgetKindId
import io.github.fiifoo.scarl.effect.TransformWidgetEffect

case class DelayedTransformingWidgetStatus(id: ActiveStatusId,
                                           tick: Int,
                                           target: ContainerId,
                                           transformTo: WidgetKindId,
                                           transformDescription: Option[String] = None
                                          ) extends ActiveStatus {

  def setTick(tick: Int): DelayedTransformingWidgetStatus = copy(tick = tick)

  def apply(s: State): List[Effect] = {
    List(TransformWidgetEffect(target, transformTo, transformDescription))
  }
}
