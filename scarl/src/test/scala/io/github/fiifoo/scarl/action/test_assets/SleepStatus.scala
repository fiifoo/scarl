package io.github.fiifoo.scarl.action.test_assets

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.Time.Tick
import io.github.fiifoo.scarl.core.effect.{Effect, TickEffect}
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.effect.area.RemoveEntityEffect
import io.github.fiifoo.scarl.effect.creature.HealEffect

case class SleepStatus(id: ActiveStatusId,
                       tick: Tick,
                       target: CreatureId
                      ) extends ActiveStatus {
  val heal = 1

  def setTick(tick: Tick): SleepStatus = copy(tick = tick)

  def apply(s: State): List[Effect] = {
    val damage = target(s).damage

    List(
      Some(TickEffect(id)),
      if (damage > 0) Some(HealEffect(target, heal, target(s).location)) else None,
      if (damage - heal <= 0) Some(RemoveEntityEffect(id)) else None
    ).flatten
  }
}
