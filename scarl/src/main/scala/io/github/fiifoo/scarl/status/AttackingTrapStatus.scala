package io.github.fiifoo.scarl.status

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.item.Discover
import io.github.fiifoo.scarl.effect.combat.TrapAttackEffect
import io.github.fiifoo.scarl.effect.movement.TriggerWidgetEffect

case class AttackingTrapStatus(id: TriggerStatusId,
                               target: ContainerId,
                               attack: Int,
                               damage: Int,
                               discover: Option[Discover] = None,
                               triggerDescription: Option[String] = None,
                               hitDescription: Option[String] = None,
                               deflectDescription: Option[String] = None,
                               missDescription: Option[String] = None,
                              ) extends TriggerStatus {

  def apply(s: State, triggerer: CreatureId): List[Effect] = {
    List(
      TriggerWidgetEffect(triggerer, target, target(s).location, discover, triggerDescription),
      TrapAttackEffect(target, triggerer, attack, damage, hitDescription, deflectDescription, missDescription),
    )
  }
}
