package io.github.fiifoo.scarl.rule

import io.github.fiifoo.scarl.rule.AttackRule.{Attacker, Defender}
import org.scalatest._

import scala.util.Random

class AttackRuleSpec extends FlatSpec with Matchers {

  "AttackRule" should "score hit with different probabilities" in {
    getHitPercentage(10, 10) should ===(50)
    getHitPercentage(20, 10) should ===(75)
    getHitPercentage(10, 20) should ===(25)
  }

  it should "score armor bypassing hit with different probabilities" in {
    getBypassPercentage(10, 10) should ===(5)
    getBypassPercentage(10, 20) should ===(5)
    getBypassPercentage(14, 10) should ===(25)
    getBypassPercentage(19, 10) should ===(50)
    getBypassPercentage(29, 10) should ===(75)
  }

  it should "deal damage with different averages" in {
    getAverageDamage(Attacker(10, 100), Defender(10, 0)) should ===(100)
    getAverageDamage(Attacker(10, 100), Defender(10, 100)) should ===(15)
    getAverageDamage(Attacker(20, 100), Defender(10, 100)) should ===(41)

    getAverageDamage(Attacker(10, 100), Defender(10, 50)) should ===(52)
    getAverageDamage(Attacker(19, 100), Defender(10, 50)) should ===(66)
  }

  private def getHitPercentage(attack: Int, defence: Int): Int = {
    val attacker = Attacker(attack, 1)
    val defender = Defender(defence, 1)

    val attacks = 100000
    val calculate = AttackRule(new Random(1)) _

    val hits = (1 to attacks).foldLeft(0)((hits, _) => {
      val result = calculate(attacker, defender)

      if (result.hit) {
        hits + 1
      } else {
        hits
      }
    })

    Math.round((hits.toDouble / attacks.toDouble) * 100).toInt
  }

  private def getBypassPercentage(attack: Int, defence: Int): Int = {
    val attacker = Attacker(attack, 1)
    val defender = Defender(defence, 1)

    val attacks = 100000
    val calculate = AttackRule(new Random(1)) _

    val (hits, bypasses) = (1 to attacks).foldLeft((0, 0))((carry, _) => {
      val (hits, bypasses) = carry
      val result = calculate(attacker, defender)

      if (result.hit) {
        if (result.bypass.isDefined) {
          (hits + 1, bypasses + 1)
        } else {
          (hits + 1, bypasses)
        }
      } else {
        (hits, bypasses)
      }
    })

    Math.round((bypasses.toDouble / hits.toDouble) * 100).toInt
  }

  private def getAverageDamage(attacker: Attacker, defender: Defender): Int = {
    val attacks = 100000
    val calculate = AttackRule(new Random(1)) _

    val (hits, damage) = (1 to attacks).foldLeft((0, 0))((carry, _) => {
      val (hits, damage) = carry
      val result = calculate(attacker, defender)

      if (result.hit) {
        (hits + 1, damage + result.damage.getOrElse(0))
      } else {
        (hits, damage)
      }
    })

    Math.round(damage.toDouble / hits.toDouble).toInt
  }
}
