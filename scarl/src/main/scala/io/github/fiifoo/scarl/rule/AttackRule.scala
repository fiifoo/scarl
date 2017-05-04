package io.github.fiifoo.scarl.rule

import io.github.fiifoo.scarl.core.Selectors.getCreatureStats
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.{Rng, State}

import scala.util.Random

object AttackRule {

  case class Attacker(attack: Int, damage: Int)

  case class Defender(defence: Int, armor: Int)

  case class Result(hit: Boolean, bypass: Option[Int], damage: Option[Int])

  val DamageVariance = 0.5

  def melee(s: State, attacker: CreatureId, defender: CreatureId): (Result, Rng) = {
    val attackerStats = getCreatureStats(s)(attacker)
    val defenderStats = getCreatureStats(s)(defender)

    AttackRule(
      s.rng,
      Attacker(attackerStats.melee.attack, attackerStats.melee.damage),
      Defender(defenderStats.defence, defenderStats.armor)
    )
  }

  def ranged(s: State, attacker: CreatureId, defender: CreatureId): (Result, Rng) = {
    val attackerStats = getCreatureStats(s)(attacker)
    val defenderStats = getCreatureStats(s)(defender)

    AttackRule(
      s.rng,
      Attacker(attackerStats.ranged.attack, attackerStats.ranged.damage),
      Defender(defenderStats.defence, defenderStats.armor)
    )
  }

  def apply(rng: Rng, attacker: Attacker, defender: Defender): (Result, Rng) = {
    val (random, nextRng) = rng()
    val hit = rollHit(random, attacker, defender)

    if (hit) {
      val bypass = rollBypass(random, attacker, defender)
      val damage = rollDamage(random, attacker, defender, bypass)

      (Result(hit, bypass, damage), nextRng)
    } else {
      (Result(hit, None, None), nextRng)
    }
  }

  private def rollHit(random: Random, attacker: Attacker, defender: Defender): Boolean = {
    random.nextInt(attacker.attack) - random.nextInt(defender.defence) match {
      case 0 => random.nextBoolean()
      case x if x > 0 => true
      case x if x < 0 => false
    }
  }

  private def rollBypass(random: Random, attacker: Attacker, defender: Defender): Option[Int] = {
    val defence = defender.defence
    val attack = Math.max(0, attacker.attack - defence) + Math.round(defence / 10)

    if (attack == 0) {
      return None
    }

    if (random.nextInt(attack) - random.nextInt(defence) match {
      case 0 => random.nextBoolean()
      case x if x > 0 => true
      case x if x < 0 => false
    }) {
      Some(random.nextInt(100) + 1)
    } else {
      None
    }
  }

  private def rollDamage(random: Random, attacker: Attacker, defender: Defender, bypass: Option[Int]): Option[Int] = {
    val variance = Math.round(attacker.damage * DamageVariance).toInt
    val damage = attacker.damage + random.nextInt(variance * 2 + 1) - variance
    val armor = defender.armor * (100 - (bypass getOrElse 0)) / 100

    if (damage > armor) {
      Some(damage - armor)
    } else {
      None
    }
  }
}
