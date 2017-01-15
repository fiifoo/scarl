package io.github.fiifoo.scarl.status

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.effect.{HealEffect, RemoveStatusEffect, TickEffect}

case class SleepStatus(id: ActiveStatusId,
                       tick: Int,
                       target: CreatureId
                      ) extends ActiveStatus {
  val interval = 100
  val heal = 1

  def setTick(tick: Int): Actor = copy(tick = tick)

  def activate(s: State): List[Effect] = {
    val damage = target(s).damage

    List(
      TickEffect(id, interval),
      if (damage > 0) HealEffect(target, heal) else null,
      if (damage - heal <= 0) RemoveStatusEffect(id) else null
    ) filter (_ != null)
  }
}
