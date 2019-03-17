package io.github.fiifoo.scarl.rule

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.entity.Selectors.getCreatureStats

import scala.util.Random

object AttackRule {

  case class Attacker(attack: Int, damage: Int)

  case class Defender(defence: Int, armor: Int)

  case class Result(hit: Boolean, bypass: Option[Int], damage: Option[Int])

  val damageVariance = 0.5
  val minBypass = 30

  def melee(s: State, random: Random)(attacker: CreatureId, defender: CreatureId): Result = {
    val attackerStats = getCreatureStats(s)(attacker)
    val defenderStats = getCreatureStats(s)(defender)

    AttackRule(random)(
      Attacker(attackerStats.melee.attack, attackerStats.melee.damage),
      Defender(defenderStats.defence, defenderStats.armor)
    )
  }

  def ranged(s: State, random: Random)(attacker: CreatureId, defender: CreatureId): Result = {
    val attackerStats = getCreatureStats(s)(attacker)
    val defenderStats = getCreatureStats(s)(defender)

    AttackRule(random)(
      Attacker(attackerStats.ranged.attack, attackerStats.ranged.damage),
      Defender(defenderStats.defence, defenderStats.armor)
    )
  }

  def apply(random: Random)(attacker: Attacker, defender: Defender): Result = {
    val hit = rollHit(random, attacker, defender)

    if (hit) {
      val bypass = rollBypass(random, attacker, defender)
      val damage = rollDamage(random, attacker, defender, bypass)

      Result(hit, bypass, damage)
    } else {
      Result(hit, None, None)
    }
  }

  private def rollHit(random: Random, attacker: Attacker, defender: Defender): Boolean = {
    val attack = math.max(attacker.attack, 1)
    val defence = math.max(defender.defence, 1)

    rollOpposedCheck(random)(attack, defence)
  }

  private def rollBypass(random: Random, attacker: Attacker, defender: Defender): Option[Int] = {
    val defence = math.max(defender.defence, 1)
    val attack = math.max(0, attacker.attack - defence) + defence / 10

    if (attack <= 0) {
      return None
    }

    if (rollOpposedCheck(random)(attack, defence)) {
      Some(minBypass + random.nextInt(100 - minBypass + 1))
    } else {
      None
    }
  }

  private def rollDamage(random: Random, attacker: Attacker, defender: Defender, bypass: Option[Int]): Option[Int] = {
    val damage = math.max(attacker.damage, 1)
    val armor = math.max(defender.armor, 0)

    val variance = math.round(damage * damageVariance).toInt
    val dealt = damage + random.nextInt(variance * 2 + 1) - variance
    val blocked = armor * (100 - (bypass getOrElse 0)) / 100

    if (dealt > blocked) {
      Some(dealt - blocked)
    } else {
      None
    }
  }
}
