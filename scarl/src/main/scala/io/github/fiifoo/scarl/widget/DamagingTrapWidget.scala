package io.github.fiifoo.scarl.widget

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.status.DamagingTrapStatus

case class DamagingTrapWidget(id: WidgetKindId,
                              item: ItemKindId,
                              damage: Int,
                              triggerDescription: String
                             ) extends WidgetKind {

  def createStatus(s: State, id: Int, target: ContainerId): Status = {
    DamagingTrapStatus(
      id = TriggerStatusId(id),
      target = target,
      damage = damage,
      triggerDescription = triggerDescription
    )
  }
}
