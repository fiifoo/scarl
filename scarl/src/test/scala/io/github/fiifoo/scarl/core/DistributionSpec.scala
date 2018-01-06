package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.math.Distribution
import io.github.fiifoo.scarl.core.math.Distribution._
import org.scalatest._

import scala.util.Random

class DistributionSpec extends FlatSpec with Matchers {

  "Distribution" should "give values with binomial distribution" in {
    val random = new Random(1)

    Binomial(10, 0).value(random) should ===(0)
    Binomial(-10, 0).value(random) should ===(0)
    Binomial(1000, 0).value(random) should ===(0)

    Binomial(0, 10).value(random) should ===(0)
    Binomial(-10, 10).value(random) should ===(0)
    Binomial(100, 10).value(random) should ===(10)
    Binomial(1000, 10).value(random) should ===(10)

    calculate(Binomial(50, 1)) should ===(Map(0 -> 50, 1 -> 50))
    calculate(Binomial(50, 2)) should ===(Map(0 -> 25, 1 -> 50, 2 -> 25))
    calculate(Binomial(10, 2)) should ===(Map(0 -> 81, 1 -> 18, 2 -> 1))
  }

  it should "give values with uniform distribution" in {
    val random = new Random(1)

    Uniform(0, 0).value(random) should ===(0)
    Uniform(-5, -5).value(random) should ===(-5)
    Uniform(5, 5).value(random) should ===(5)
    intercept[Exception] {
      Uniform(2, 1).value(random)
    }

    calculate(Uniform(0, 1)) should ===(Map(0 -> 50, 1 -> 50))
    calculate(Uniform(1, 4)) should ===(Map(1 -> 25, 2 -> 25, 3 -> 25, 4 -> 25))
  }

  private def calculate(distribution: Distribution, iterations: Int = 10000): Map[Int, Int] = {
    val random = new Random(1)

    val results = ((0 until iterations) foldLeft Map[Int, Int]()) ((results, _) => {
      val value = distribution.value(random)
      val count = results.getOrElse(value, 0) + 1

      results + (value -> count)
    })

    results mapValues (count => Math.round(count.toFloat * 100 / iterations))
  }
}
