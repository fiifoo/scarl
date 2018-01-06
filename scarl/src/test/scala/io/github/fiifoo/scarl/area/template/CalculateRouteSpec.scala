package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.shape.Rectangle
import io.github.fiifoo.scarl.core.geometry.Location
import org.scalatest._

import scala.util.Random

class CalculateRouteSpec extends FlatSpec with Matchers {

  "CalculateRoute" should "calculate route from one location to another" in {
    val random = new Random(1)

    val from = Location(0, 0)
    val to = Location(4, 0)
    val area = Rectangle(50, 50, 0)(random).contained.map(Location(-25, -25).add)
    val locations = area -- Set(
      Location(2, 0),
      Location(2, 1),
      Location(2, 2),
      Location(2, -1),
      Location(2, -2),
      Location(2, -3)
    )

    val expected = Set(
      Location(1, 0),
      Location(1, 1),
      Location(1, 2),
      Location(1, 3),
      Location(2, 3),
      Location(3, 3),
      Location(4, 3),
      Location(4, 2),
      Location(4, 1)
    )

    val result = CalculateRoute(from: Location, to: Location, locations).get

    result -- expected should ===(Set())
  }
}
