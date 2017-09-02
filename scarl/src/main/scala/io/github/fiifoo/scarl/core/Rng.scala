package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.Rng.WeightedChoices

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

  def nextChoice[T](choices: WeightedChoices[T]): (T, Rng) = {
    val random = new Random(seed)

    (Rng.nextChoice(random, choices), Rng(random.nextInt()))
  }

  def nextChoice[T](choices: Iterable[T]): (T, Rng) = {
    val random = new Random(seed)

    (Rng.nextChoice(random, choices), Rng(random.nextInt()))
  }

  def nextRange(min: Int, max: Int): (Range, Rng) = {
    val random = new Random(seed)

    (Rng.nextRange(random, min, max), Rng(random.nextInt()))
  }
}

object Rng {

  def nextChoice[T](random: Random, choices: WeightedChoices[T]): T = {
    choices(random)
  }

  def nextChoice[T](random: Random, choices: Iterable[T]): T = {
    val n = random.nextInt(choices.size)

    choices.iterator.drop(n).next
  }

  def nextRange(random: Random, min: Int, max: Int): Range = {
    0 until min + random.nextInt(max - min + 1)
  }

  case class WeightedChoice[T](value: T, weight: Int)

  case class WeightedChoices[T](choices: Iterable[WeightedChoice[T]]) {
    type BuildResult = (List[WeightedChoice[T]], Int)

    val (cumulative, total) = build()

    def apply(random: Random): T = {
      val value = random.nextInt(total) + 1

      val result = cumulative find (choice => {
        choice.weight >= value
      })

      result.get.value
    }

    private def build(): BuildResult = {
      val (cumulative, total) = choices.foldLeft[BuildResult]((List(), 0))((result, choice) => {
        val total = result._2 + choice.weight
        val cumulative = WeightedChoice(choice.value, total) :: result._1

        (cumulative, total)
      })

      (cumulative.reverse, total)
    }
  }

}
