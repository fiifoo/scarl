package io.github.fiifoo.scarl.status

import io.github.fiifoo.scarl.core.Selectors.getLocationEntities
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, TickEffect}
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.WidgetKindId
import io.github.fiifoo.scarl.effect.area.{RemoveEntityEffect, TransformEffect}
import io.github.fiifoo.scarl.effect.creature.HealEffect

case class HealLocationStatus(id: ActiveStatusId,
                              tick: Int,
                              target: ContainerId,
                              amount: Int,
                              interval: Int,
                              expireAt: Option[Int] = None,
                              transformTo: Option[WidgetKindId] = None,
                              transformDescription: Option[String] = None
                             ) extends ActiveStatus {

  def setTick(tick: Int): HealLocationStatus = copy(tick = tick)

  def apply(s: State): List[Effect] = {
    val location = target(s).location
    val effects = getLocationEntities(s)(location).toList collect {
      case target: CreatureId => HealEffect(target, amount, location)
    }

    if (expireAt.forall(_ > tick)) {
      TickEffect(id, interval) :: effects
    } else if (transformTo.isDefined) {
      effects ::: List(TransformEffect(
        from = target,
        to = transformTo.get,
        description = transformDescription
      ))
    } else {
      RemoveEntityEffect(id) :: effects
    }
  }
}
