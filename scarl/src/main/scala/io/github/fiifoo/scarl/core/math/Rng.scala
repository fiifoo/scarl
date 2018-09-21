package io.github.fiifoo.scarl.core.math

import io.github.fiifoo.scarl.core.math.Rng._

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

  def nextChoices[T](random: Random, choices: Set[T], count: Int): Set[T] = {
    val (results, _) = ((0 until count) foldLeft(Set[T](), choices)) ((carry, _) => {
      val (results, choices) = carry
      val choice = nextChoice(random, choices)

      (results + choice, choices - choice)
    })

    results
  }

  def nextRange(random: Random, min: Int, max: Int): Range = {
    0 until min + random.nextInt(max - min + 1)
  }

  def nextRange(random: Random, distribution: Distribution): Range = {
    0 until distribution.value(random)
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
