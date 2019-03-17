package io.github.fiifoo.scarl.rule

import scala.util.Random

object ConditionRule {

  val MinimumStrength = 5

  def apply(random: Random)(strength: Int, resistance: Int): Option[Int] = {
    if (resistance <= 0) {
      return Some(strength)
    }

    val result = strength + rollOpposedValue(random)(strength, resistance)

    if (result > strength) {
      Some(strength)
    } else if (result >= MinimumStrength) {
      Some(result)
    } else {
      None
    }
  }
}
