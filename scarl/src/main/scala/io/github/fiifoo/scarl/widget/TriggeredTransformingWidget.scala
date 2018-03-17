package io.github.fiifoo.scarl.widget

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.WidgetKind.Category
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.status.TriggeredTransformingWidgetStatus

case class TriggeredTransformingWidget(id: WidgetKindId,
                                       item: ItemKindId,
                                       category: Option[Category] = None,
                                       power: Option[Int] = None,
                                       transformTo: WidgetKindId,
                                       transformDescription: Option[String] = None
                                      ) extends WidgetKind {

  def createStatus(s: State, id: Int, target: ContainerId): Status = {
    TriggeredTransformingWidgetStatus(
      id = TriggerStatusId(id),
      target = target,
      transformTo = transformTo,
      transformDescription = transformDescription
    )
  }
}
