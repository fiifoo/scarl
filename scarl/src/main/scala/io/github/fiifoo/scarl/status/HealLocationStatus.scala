package io.github.fiifoo.scarl.status

import io.github.fiifoo.scarl.core.Selectors.getLocationEntities
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.WidgetKindId
import io.github.fiifoo.scarl.effect.{HealEffect, RemoveStatusEffect, TickEffect, TransformWidgetEffect}

case class HealLocationStatus(id: ActiveStatusId,
                              tick: Int,
                              target: ContainerId,
                              amount: Int,
                              interval: Int,
                              expireAt: Option[Int] = None,
                              transformTo: Option[WidgetKindId] = None,
                              transformDescription: Option[String] = None
                             ) extends ActiveStatus {

  def setTick(tick: Int): Actor = copy(tick = tick)

  def apply(s: State): List[Effect] = {
    val location = target(s).location
    val effects = getLocationEntities(s)(location) collect {
      case target: CreatureId => HealEffect(target, amount)
    }

    if (expireAt.forall(_ > tick)) {
      TickEffect(id, interval) :: effects
    } else if (transformTo.isDefined) {
      effects ::: List(TransformWidgetEffect(target, transformTo.get, transformDescription))
    } else {
      RemoveStatusEffect(id) :: effects
    }
  }
}
