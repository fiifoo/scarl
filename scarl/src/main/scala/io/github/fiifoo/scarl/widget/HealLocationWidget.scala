package io.github.fiifoo.scarl.widget

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.{ItemKindId, WidgetKind, WidgetKindId}
import io.github.fiifoo.scarl.status.HealLocationStatus

case class HealLocationWidget(id: WidgetKindId,
                              item: ItemKindId,
                              amount: Int,
                              interval: Int,
                              duration: Option[Int],
                              transformTo: Option[WidgetKindId],
                              transformDescription: Option[String] = None
                             ) extends WidgetKind {

  def createStatus(s: State, id: Int, target: ContainerId): Status = {
    HealLocationStatus(
      id = ActiveStatusId(id),
      tick = s.tick,
      target = target,
      amount: Int,
      interval: Int,
      expireAt = duration map (s.tick + _),
      transformTo = transformTo,
      transformDescription = transformDescription
    )
  }
}
