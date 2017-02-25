package io.github.fiifoo.scarl.core.test_assets

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity._

case class TestTriggerStatus(id: TriggerStatusId,
                             target: ContainerId
                            ) extends TriggerStatus {
  val damage = 1

  def apply(s: State, triggerer: CreatureId): List[Effect] = {
    List(
      TestDamageEffect(triggerer, damage)
    )
  }
}
