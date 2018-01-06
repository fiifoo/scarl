package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.{CreatureId, Selectors}
import io.github.fiifoo.scarl.effect.TickEffect
import io.github.fiifoo.scarl.effect.combat.ExplodeEffect

case object ExplodeAction extends Action {
  def apply(s: State, actor: CreatureId): List[Effect] = {
    val explosive = Selectors.getCreatureStats(s)(actor).explosive

    List(
      TickEffect(actor),
      ExplodeEffect(actor, actor(s).location, explosive)
    )
  }
}
