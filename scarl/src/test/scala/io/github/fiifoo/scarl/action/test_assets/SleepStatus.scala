package io.github.fiifoo.scarl.action.test_assets

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, TickEffect}
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.effect.area.RemoveEntityEffect
import io.github.fiifoo.scarl.effect.creature.HealEffect

case class SleepStatus(id: ActiveStatusId,
                       tick: Int,
                       target: CreatureId
                      ) extends ActiveStatus {
  val interval = 100
  val heal = 1

  def setTick(tick: Int): SleepStatus = copy(tick = tick)

  def apply(s: State): List[Effect] = {
    val damage = target(s).damage

    List(
      Some(TickEffect(id, interval)),
      if (damage > 0) Some(HealEffect(target, heal, target(s).location)) else None,
      if (damage - heal <= 0) Some(RemoveEntityEffect(id)) else None
    ).flatten
  }
}
