package io.github.fiifoo.scarl.rule

import scala.util.Random

object DetectRule {

  val maxDistance = 10

  def apply(random: Random)(sensors: Int, concealment: Int, distance: Int): Boolean = {
    val effective = if (distance <= 1) sensors else sensors / distance

    if (distance > maxDistance || effective <= 0 || concealment <= 0) {
      false
    } else {
      rollOpposedCheck(random)(effective, concealment)
    }
  }
}
