package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.action.Utils._
import io.github.fiifoo.scarl.core.Selectors.getCreatureStats
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.{Effect, TickEffect}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.effect.combat.StrikeEffect

case class AttackAction(target: CreatureId) extends Action {
  def apply(s: State, actor: CreatureId): List[Effect] = {
    val creature = actor(s)
    val consumption = getCreatureStats(s)(actor).melee.consumption
    val tick = TickEffect(actor)

    shortage(creature, consumption) map (List(tick, _)) getOrElse {
      List(
        Some(tick),
        Some(StrikeEffect(actor, target)),
        consume(creature, consumption)
      ).flatten
    }
  }
}
