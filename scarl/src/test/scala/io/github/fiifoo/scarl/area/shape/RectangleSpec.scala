package io.github.fiifoo.scarl.area.shape

import io.github.fiifoo.scarl.area.shape.Shape.Result
import io.github.fiifoo.scarl.core.Location
import org.scalatest._

import scala.util.Random

class RectangleSpec extends FlatSpec with Matchers {

  "Rectangle" should "calculate rectangles" in {
    val v1 = Rectangle(1, 1, 0)
    val r1 = Result(
      outerWidth = 1,
      outerHeight = 1,
      innerWidth = 1,
      innerHeight = 1,
      border = Set(Location(0, 0)),
      contained = Set(),
      entranceCandidates = Set()
    )

    v1(new Random(1)) should ===(r1)

    val v2 = Rectangle(2, 2, 0)
    val r2 = Result(
      outerWidth = 2,
      outerHeight = 2,
      innerWidth = 2,
      innerHeight = 2,
      border = Set(Location(0, 0), Location(1, 0), Location(0, 1), Location(1, 1)),
      contained = Set(),
      entranceCandidates = Set()
    )

    v2(new Random(1)) should ===(r2)

    val v3 = Rectangle(3, 3, 0)
    val r3 = Result(
      outerWidth = 3,
      outerHeight = 3,
      innerWidth = 3,
      innerHeight = 3,
      border = Set(Location(0, 2), Location(0, 0), Location(2, 0), Location(2, 2), Location(0, 1), Location(1, 2), Location(2, 1), Location(1, 0)),
      contained = Set(Location(1, 1)),
      entranceCandidates = Set(Location(0, 1), Location(1, 2), Location(2, 1), Location(1, 0))
    )

    v3(new Random(1)) should ===(r3)

    val v4 = Rectangle(4, 3, 0)
    val r4 = Result(
      outerWidth = 4,
      outerHeight = 3,
      innerWidth = 4,
      innerHeight = 3,
      border = Set(Location(0, 2), Location(0, 0), Location(3, 1), Location(2, 0), Location(3, 0), Location(3, 2), Location(2, 2), Location(0, 1), Location(1, 2), Location(1, 0)),
      contained = Set(Location(1, 1), Location(2, 1)),
      entranceCandidates = Set(Location(3, 1), Location(2, 0), Location(2, 2), Location(0, 1), Location(1, 2), Location(1, 0))
    )

    v4(new Random(1)) should ===(r4)
  }

  it should "calculate rectangles with variance" in {
    val v1 = Rectangle(2, 2, 0.5)
    val r1 = Result(
      outerWidth = 1,
      outerHeight = 2,
      innerWidth = 1,
      innerHeight = 2,
      border = Set(Location(0, 0), Location(0, 1)),
      contained = Set(),
      entranceCandidates = Set()
    )

    v1(new Random(1)) should ===(r1)

    val r2 = Result(
      outerWidth = 3,
      outerHeight = 3,
      innerWidth = 3,
      innerHeight = 3,
      border = Set(Location(0, 2), Location(0, 0), Location(2, 0), Location(2, 2), Location(0, 1), Location(1, 2), Location(2, 1), Location(1, 0)),
      contained = Set(Location(1, 1)),
      entranceCandidates = Set(Location(0, 1), Location(1, 2), Location(2, 1), Location(1, 0))
    )

    v1(new Random(424)) should ===(r2)
  }
}
