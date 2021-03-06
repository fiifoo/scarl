package io.github.fiifoo.scarl.ai.intention

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Intention
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId, SafeCreatureId}

import scala.util.Random

case class CheckAttackIntention(enableMove: Boolean = true) extends Intention {

  def apply(s: State, actor: CreatureId, random: Random): Option[Result] = {
    Utils.findVisibleEnemy(s, actor) flatMap attack(s, actor, random)
  }

  private def attack(s: State, actor: CreatureId, random: Random)(enemy: Creature): Option[Result] = {
    val target = SafeCreatureId(enemy.id)

    if (enableMove) {
      AttackIntention(target)(s, actor, random) orElse ChargeIntention(target)(s, actor, random)
    } else {
      AttackIntention(target, enableMove = false)(s, actor, random)
    }
  }
}
