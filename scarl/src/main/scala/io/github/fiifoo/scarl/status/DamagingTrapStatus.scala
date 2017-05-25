package io.github.fiifoo.scarl.status

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.effect.{DamageEffect, TriggerTrapEffect}

case class DamagingTrapStatus(id: TriggerStatusId,
                              target: ContainerId,
                              damage: Int,
                              triggerDescription: String
                             ) extends TriggerStatus {

  def apply(s: State, triggerer: CreatureId): List[Effect] = {
    List(
      TriggerTrapEffect(triggerer, target, triggerDescription),
      DamageEffect(triggerer, damage)
    )
  }
}
