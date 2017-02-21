package io.github.fiifoo.scarl.widget

import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.{ItemKindId, WidgetKind, WidgetKindId}
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.status.DelayedTransformingWidgetStatus

case class DelayedTransformingWidget(id: WidgetKindId,
                                     item: ItemKindId,
                                     transformTo: WidgetKindId,
                                     transformDescription: Option[String] = None,
                                     delay: Int
                                    ) extends WidgetKind {

  def apply(s: State, location: Location): (Container, Item, DelayedTransformingWidgetStatus) = {
    val (container, _item) = item(s)(s, location)

    val status = DelayedTransformingWidgetStatus(
      id = ActiveStatusId(s.nextEntityId + 2),
      tick = s.tick + delay,
      target = container.id,
      transformTo = transformTo,
      transformDescription = transformDescription
    )

    (container, _item, status)
  }
}
