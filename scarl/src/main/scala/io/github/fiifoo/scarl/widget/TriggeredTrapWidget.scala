package io.github.fiifoo.scarl.widget

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.item.Discover
import io.github.fiifoo.scarl.core.kind.WidgetKind.Category
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.status.TriggeredTrapStatus

case class TriggeredTrapWidget(id: WidgetKindId,
                               item: ItemKindId,
                               smart: Boolean = false,
                               category: Option[Category] = None,
                               power: Option[Int] = None,
                               discover: Option[Discover] = None,
                               triggerDescription: Option[String] = None,
                              ) extends WidgetKind {

  def createStatus(s: State, id: Int, target: ContainerId): Status = {
    TriggeredTrapStatus(
      id = TriggerStatusId(id),
      target,
      smart,
      discover,
      triggerDescription
    )
  }
}
