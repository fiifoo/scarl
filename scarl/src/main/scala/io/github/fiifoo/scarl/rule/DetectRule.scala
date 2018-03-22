package io.github.fiifoo.scarl.rule

import scala.util.Random

object DetectRule {

  val maxDistance = 10

  def apply(random: Random)(sensors: Int, concealment: Int, distance: Int): Boolean = {
    val effective = if (distance <= 1) sensors else sensors / distance

    if (distance > maxDistance || effective <= 0 || concealment <= 0) {
      false
    } else {
      random.nextInt(effective) - random.nextInt(concealment) match {
        case 0 => random.nextBoolean()
        case x if x > 0 => true
        case x if x < 0 => false
      }
    }
  }
}
