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

  def setTick(tick: Int): SleepStatus = copy(tick = tick)

  def apply(s: State): List[Effect] = {
    val damage = target(s).damage

    List(
      Some(TickEffect(id, interval)),
      if (damage > 0) Some(HealEffect(target, heal)) else None,
      if (damage - heal <= 0) Some(RemoveStatusEffect(id)) else None
    ).flatten
  }
}
