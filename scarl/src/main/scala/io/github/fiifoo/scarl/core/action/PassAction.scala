package io.github.fiifoo.scarl.core.action

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, TickEffect}
import io.github.fiifoo.scarl.core.entity.CreatureId

case object PassAction extends Action {
  val cost = 100

  def apply(s: State, actor: CreatureId): List[Effect] = {
    List(TickEffect(actor, cost))
  }
}
