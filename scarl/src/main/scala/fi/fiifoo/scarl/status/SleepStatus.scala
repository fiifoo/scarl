package fi.fiifoo.scarl.status

import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.effect.Effect
import fi.fiifoo.scarl.core.entity._
import fi.fiifoo.scarl.effect.{HealEffect, RemoveStatusEffect, TickEffect}

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
