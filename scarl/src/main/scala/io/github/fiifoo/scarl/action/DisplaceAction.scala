package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.{Effect, TickEffect}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.effect.DisplaceEffect
import io.github.fiifoo.scarl.rule.MoveRule

case class DisplaceAction(target: CreatureId) extends Action {
  def apply(s: State, actor: CreatureId): List[Effect] = {
    val cost = MoveRule.cost(s, actor)

    List(
      TickEffect(actor, cost),
      DisplaceEffect(actor, target)
    )
  }
}
