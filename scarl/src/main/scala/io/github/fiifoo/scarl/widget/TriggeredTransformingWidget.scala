package io.github.fiifoo.scarl.widget

import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.status.TriggeredTransformingWidgetStatus

case class TriggeredTransformingWidget(id: WidgetKindId,
                                       item: ItemKindId,
                                       transformTo: WidgetKindId,
                                       transformDescription: Option[String] = None
                                      ) extends WidgetKind {

  def apply(s: State, location: Location): (Container, Item, TriggeredTransformingWidgetStatus) = {
    val (container, _item) = item(s)(s, location)

    val status = TriggeredTransformingWidgetStatus(
      id = TriggerStatusId(s.nextEntityId + 2),
      target = container.id,
      transformTo = transformTo,
      transformDescription = transformDescription
    )

    (container, _item, status)
  }
}
