package fi.fiifoo.scarl.core.test_assets

import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.effect.Effect
import fi.fiifoo.scarl.core.entity._

case class TestActiveStatus(id: ActiveStatusId,
                            tick: Int,
                            target: CreatureId
                           ) extends ActiveStatus {
  val interval = 50
  val damage = 1

  def setTick(tick: Int): Actor = copy(tick = tick)

  def activate(s: State): List[Effect] = {
    List(
      TestTickEffect(id, interval),
      TestDamageEffect(target, damage)
    )
  }
}
