package io.github.fiifoo.scarl.widget

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.Time.Tick
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.{ItemKindId, WidgetKind, WidgetKindId}
import io.github.fiifoo.scarl.status.TimedExplosiveStatus

case class TimedExplosiveWidget(id: WidgetKindId,
                                item: ItemKindId,
                                timer: Tick
                               ) extends WidgetKind {

  def createStatus(s: State, id: Int, target: ContainerId): Status = {
    TimedExplosiveStatus(
      id = ActiveStatusId(id),
      tick = s.tick,
      target = target,
      explodeAt = s.tick + timer
    )
  }
}
