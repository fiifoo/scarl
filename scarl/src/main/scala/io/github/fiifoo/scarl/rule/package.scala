package io.github.fiifoo.scarl

import scala.util.Random

package object rule {

  def rollOpposedCheck(random: Random)(a: Int, b: Int): Boolean = {
    rollOpposedValue(random)(a, b) match {
      case 0 => random.nextBoolean()
      case x if x > 0 => true
      case x if x < 0 => false
    }
  }

  def rollOpposedValue(random: Random)(a: Int, b: Int): Int = {
    random.nextInt(a) - random.nextInt(b)
  }
}
