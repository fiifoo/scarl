package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.{Effect, TickEffect}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.effect.UseCreatureEffect

case class UseCreatureAction(target: CreatureId) extends Action {
  val cost = 100

  def apply(s: State, actor: CreatureId): List[Effect] = {
    List(
      TickEffect(actor, cost),
      UseCreatureEffect(actor, target)
    )
  }
}
