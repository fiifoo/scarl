package io.github.fiifoo.scarl.widget

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.item.Discover
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.status.TriggeredMachineryStatus

case class TriggeredMachineryWidget(id: WidgetKindId,
                                    item: ItemKindId,
                                    discover: Option[Discover] = None,
                                    description: Option[String] = None,
                                   ) extends WidgetKind {

  def createStatus(s: State, id: Int, target: ContainerId): Status = {
    TriggeredMachineryStatus(
      TriggerStatusId(id),
      target,
      discover,
      description,
    )
  }
}
