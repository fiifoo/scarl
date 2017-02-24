package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.Rng.WeightedChoices
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

  it should "give weighted choice" in {
    val d1 = getChoiceDistribution(WeightedChoices(List(
      ('A', 1),
      ('B', 1),
      ('C', 1)
    )))

    Math.round(d1('A').toDouble / d1('B').toDouble) should ===(1)
    Math.round(d1('A').toDouble / d1('C').toDouble) should ===(1)
    Math.round(d1('B').toDouble / d1('C').toDouble) should ===(1)

    val d2 = getChoiceDistribution(WeightedChoices(List(
      ('A', 10),
      ('B', 2),
      ('C', 1)
    )))

    Math.round(d2('A').toDouble / d2('B').toDouble) should ===(5)
    Math.round(d2('A').toDouble / d2('C').toDouble) should ===(10)
    Math.round(d2('B').toDouble / d2('C').toDouble) should ===(2)
  }

  private def getChoiceDistribution(choices: WeightedChoices[Char]): Map[Char, Int] = {
    val initial = Map('A' -> 0, 'B' -> 0, 'C' -> 0)

    val (result, _) = (1 to 100000).foldLeft[(Map[Char, Int], Rng)]((initial, Rng(1)))((carry, _) => {
      val (current, rng) = carry
      val (choice, nextRng) = rng.nextChoice(choices)

      (current + (choice -> (current(choice) + 1)), nextRng)
    })

    result
  }
}
