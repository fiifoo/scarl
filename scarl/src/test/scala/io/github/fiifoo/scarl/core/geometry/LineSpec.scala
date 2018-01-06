package io.github.fiifoo.scarl.core.geometry

import org.scalatest._

import scala.util.Random

class LineSpec extends FlatSpec with Matchers {

  "Line" should "calculate line" in {
    Line(Location(0, 0), Location(2, 2)) should ===(Vector(Location(0, 0), Location(1, 1), Location(2, 2)))
    Line(Location(0, 0), Location(2, 1)) should ===(Vector(Location(0, 0), Location(1, 1), Location(2, 1)))

    val random = new Random()

    for (_ <- 0 to 1000) {
      val a = generateLocation(random)
      val b = generateLocation(random)
      val line = Line(a, b)

      line.head should ===(a)
      line.last should ===(b)

      val dx = Math.abs(b.x - a.x)
      val dy = Math.abs(b.y - a.y)
      line.size should ===(Math.max(dx, dy) + 1)
    }
  }

  def generateLocation(random: Random): Location = {
    val x = random.nextInt(50) - 25
    val y = random.nextInt(50) - 25

    Location(x, y)
  }
}
