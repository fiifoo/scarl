package io.github.fiifoo.scarl.widget

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.Time.Tick
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.WidgetKind.Category
import io.github.fiifoo.scarl.core.kind.{ItemKindId, WidgetKind, WidgetKindId}
import io.github.fiifoo.scarl.status.DelayedTransformingWidgetStatus

case class DelayedTransformingWidget(id: WidgetKindId,
                                     item: ItemKindId,
                                     category: Option[Category] = None,
                                     power: Option[Int] = None,
                                     transformTo: WidgetKindId,
                                     transformDescription: Option[String] = None,
                                     delay: Tick
                                    ) extends WidgetKind {

  def createStatus(s: State, id: Int, target: ContainerId): Status = {
    DelayedTransformingWidgetStatus(
      id = ActiveStatusId(id),
      tick = s.tick + delay,
      target = target,
      transformTo = transformTo,
      transformDescription = transformDescription
    )
  }
}
