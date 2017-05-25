package io.github.fiifoo.scarl.widget

import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.status.DamagingTrapStatus

case class DamagingTrapWidget(id: WidgetKindId,
                              item: ItemKindId,
                              damage: Int,
                              triggerDescription: String
                             ) extends WidgetKind {

  def apply(s: State, location: Location): (Container, Item, DamagingTrapStatus) = {
    val (container, _item) = item(s)(s, location)

    val status = DamagingTrapStatus(
      id = TriggerStatusId(s.nextEntityId + 2),
      target = container.id,
      damage = damage,
      triggerDescription = triggerDescription
    )

    (container, _item, status)
  }
}
