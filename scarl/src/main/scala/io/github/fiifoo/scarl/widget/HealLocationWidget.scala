package io.github.fiifoo.scarl.widget

import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.{ItemKindId, WidgetKind, WidgetKindId}
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.status.HealLocationStatus

case class HealLocationWidget(id: WidgetKindId,
                              item: ItemKindId,
                              amount: Int,
                              interval: Int,
                              duration: Option[Int],
                              transformTo: Option[WidgetKindId],
                              transformDescription: Option[String] = None
                             ) extends WidgetKind {

  def apply(s: State, location: Location): (Container, Item, HealLocationStatus) = {
    val (container, _item) = item(s)(s, location)

    val status = HealLocationStatus(
      id = ActiveStatusId(s.nextEntityId + 2),
      tick = s.tick,
      target = container.id,
      amount: Int,
      interval: Int,
      expireAt = duration map (s.tick + _),
      transformTo = transformTo,
      transformDescription = transformDescription
    )

    (container, _item, status)
  }
}
