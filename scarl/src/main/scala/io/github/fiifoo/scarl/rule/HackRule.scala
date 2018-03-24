package io.github.fiifoo.scarl.rule

import scala.util.Random

object HackRule {
  def apply(random: Random)(skill: Int, security: Int): Boolean = {
    if (skill <= 0 || security <= 0) {
      false
    } else {
      random.nextInt(skill) - random.nextInt(security) match {
        case 0 => random.nextBoolean()
        case x if x > 0 => true
        case x if x < 0 => false
      }
    }
  }
}
