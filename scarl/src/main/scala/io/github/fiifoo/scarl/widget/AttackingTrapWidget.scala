package io.github.fiifoo.scarl.widget

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.item.Discover
import io.github.fiifoo.scarl.core.kind.WidgetKind.Category
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.status.AttackingTrapStatus

case class AttackingTrapWidget(id: WidgetKindId,
                               item: ItemKindId,
                               category: Option[Category] = None,
                               power: Option[Int] = None,
                               attack: Int,
                               damage: Int,
                               discover: Option[Discover] = None,
                               triggerDescription: Option[String] = None,
                               hitDescription: Option[String] = None,
                               deflectDescription: Option[String] = None,
                               missDescription: Option[String] = None,
                              ) extends WidgetKind {

  def createStatus(s: State, id: Int, target: ContainerId): Status = {
    AttackingTrapStatus(
      id = TriggerStatusId(id),
      target,
      attack,
      damage,
      discover,
      triggerDescription,
      hitDescription,
      deflectDescription,
      missDescription
    )
  }
}
