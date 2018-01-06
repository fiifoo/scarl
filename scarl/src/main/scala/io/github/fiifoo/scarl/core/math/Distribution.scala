package io.github.fiifoo.scarl.core.math

import scala.util.Random

sealed trait Distribution {
  def value(random: Random): Int
}

object Distribution {

  case class Binomial(p: Int, n: Int) extends Distribution {
    def value(random: Random): Int = (0 until n) count (_ => random.nextInt(100) < p)
  }

  case class Uniform(min: Int, max: Int) extends Distribution {
    def value(random: Random): Int = min + random.nextInt(max - min + 1)
  }

}
