package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.core.math.Rng.{WeightedChoice, WeightedChoices}
import org.scalatest._

class RngSpec extends FlatSpec with Matchers {

  "Rng" should "give seeded random numbers" in {
    val rng = Rng(1)
    val (a, _) = rng.nextInt(1000)
    val (b, _) = rng.nextInt(1000)

    a should ===(b)
  }

  it should "give copy of itself with new seed" in {
    val rng1 = Rng(1)
    val (a, rng2) = rng1.nextInt(1000)
    val (b, _) = rng2.nextInt(1000)

    a should !==(b)
  }

  it should "give range with min/max length" in {
    val min = 10
    val max = 20

    val (minLength, maxLength, _) = (1 to 1000).foldLeft[(Int, Int, Rng)]((999, 0, Rng(1)))((carry, _) => {
      val (minLength, maxLength, rng) = carry
      val (range, nextRng) = rng.nextRange(min, max)

      (Math.min(range.length, minLength), Math.max(range.length, maxLength), nextRng)
    })

    minLength should ===(min)
    maxLength should ===(max)
  }

  it should "give choices from set" in {
    val d = getSetChoiceDistribution(Set('A', 'B', 'C'))

    Math.round(d('A').toDouble / d('B').toDouble) should ===(1)
    Math.round(d('A').toDouble / d('C').toDouble) should ===(1)
    Math.round(d('B').toDouble / d('C').toDouble) should ===(1)
  }

  it should "give weighted choice" in {
    val d1 = getWeightedChoiceDistribution(WeightedChoices(List(
      WeightedChoice('A', 1),
      WeightedChoice('B', 1),
      WeightedChoice('C', 1)
    )))

    Math.round(d1('A').toDouble / d1('B').toDouble) should ===(1)
    Math.round(d1('A').toDouble / d1('C').toDouble) should ===(1)
    Math.round(d1('B').toDouble / d1('C').toDouble) should ===(1)

    val d2 = getWeightedChoiceDistribution(WeightedChoices(List(
      WeightedChoice('A', 10),
      WeightedChoice('B', 2),
      WeightedChoice('C', 1)
    )))

    Math.round(d2('A').toDouble / d2('B').toDouble) should ===(5)
    Math.round(d2('A').toDouble / d2('C').toDouble) should ===(10)
    Math.round(d2('B').toDouble / d2('C').toDouble) should ===(2)
  }

  private def getSetChoiceDistribution(choices: Set[Char]): Map[Char, Int] = {
    def next(rng: Rng): (Char, Rng) = rng.nextChoice(choices)

    getChoiceDistribution(next)
  }

  private def getWeightedChoiceDistribution(choices: WeightedChoices[Char]): Map[Char, Int] = {
    def next(rng: Rng): (Char, Rng) = rng.nextChoice(choices)

    getChoiceDistribution(next)
  }

  private def getChoiceDistribution(next: Rng => (Char, Rng)): Map[Char, Int] = {
    val initial = Map('A' -> 0, 'B' -> 0, 'C' -> 0)

    val (result, _) = (1 to 100000).foldLeft[(Map[Char, Int], Rng)]((initial, Rng(1)))((carry, _) => {
      val (current, rng) = carry
      val (choice, nextRng) = next(rng)

      (current + (choice -> (current(choice) + 1)), nextRng)
    })

    result
  }
}
