package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Creature
import io.github.fiifoo.scarl.effect.{DamageEffect, TickEffect}

case class AttackAction(target: Creature) extends Action {
  val cost = 200
  val damage = 1

  def apply(s: State, actor: Creature): List[Effect] = {
    List(
      TickEffect(actor.id, cost),
      DamageEffect(target.id, damage)
    )
  }
}
