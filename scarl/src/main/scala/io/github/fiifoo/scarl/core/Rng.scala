package io.github.fiifoo.scarl.core

import scala.util.Random

case class Rng(seed: Int) {

  def apply(): (Random, Rng) = {
    val random = new Random(seed)

    (random, Rng(random.nextInt()))
  }

  def nextInt(n: Int): (Int, Rng) = {
    val random = new Random(seed)

    (random.nextInt(n), Rng(random.nextInt()))
  }

}
