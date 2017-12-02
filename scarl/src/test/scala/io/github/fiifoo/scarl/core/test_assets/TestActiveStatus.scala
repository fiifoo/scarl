package io.github.fiifoo.scarl.core.test_assets

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.Time.Tick
import io.github.fiifoo.scarl.core.effect.{Effect, TickEffect}
import io.github.fiifoo.scarl.core.entity._

case class TestActiveStatus(id: ActiveStatusId,
                            tick: Tick,
                            target: CreatureId
                           ) extends ActiveStatus {
  val interval = 50
  val damage = 1

  def setTick(tick: Tick): TestActiveStatus = copy(tick = tick)

  def apply(s: State): List[Effect] = {
    List(
      TickEffect(id, interval),
      TestDamageEffect(target, damage)
    )
  }
}
