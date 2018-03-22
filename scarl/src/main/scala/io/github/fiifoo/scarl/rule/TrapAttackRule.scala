package io.github.fiifoo.scarl.rule

import io.github.fiifoo.scarl.rule.AttackRule.{Attacker, Defender, Result}

import scala.util.Random

object TrapAttackRule {

  val evadeMultiplier = 1.5

  def apply(random: Random)(attacker: Attacker, defender: Defender, evade: Boolean): Result = {
    AttackRule(random)(attacker, defender.copy(
      defence = if (evade) (defender.defence * evadeMultiplier).toInt else defender.defence
    ))
  }

}
