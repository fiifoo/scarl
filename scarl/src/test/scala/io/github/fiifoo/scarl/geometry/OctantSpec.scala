package io.github.fiifoo.scarl.geometry

import io.github.fiifoo.scarl.core.Location
import org.scalatest._

import scala.util.Random

class OctantSpec extends FlatSpec with Matchers {

  "Octant" should "calculate line octants" in {
    Octant(Location(0, 0), Location(0, 0)) should ===(Octant(0))

    Octant(Location(0, 0), Location(2, 0)) should ===(Octant(0))
    Octant(Location(0, 0), Location(2, 1)) should ===(Octant(0))
    Octant(Location(0, 0), Location(2, 2)) should ===(Octant(1))
    Octant(Location(0, 0), Location(1, 2)) should ===(Octant(1))

    Octant(Location(0, 0), Location(0, 2)) should ===(Octant(2))
    Octant(Location(0, 0), Location(-1, 2)) should ===(Octant(2))
    Octant(Location(0, 0), Location(-2, 2)) should ===(Octant(3))
    Octant(Location(0, 0), Location(-2, 1)) should ===(Octant(3))

    Octant(Location(0, 0), Location(-2, 0)) should ===(Octant(4))
    Octant(Location(0, 0), Location(-2, -1)) should ===(Octant(4))
    Octant(Location(0, 0), Location(-2, -2)) should ===(Octant(5))
    Octant(Location(0, 0), Location(-1, -2)) should ===(Octant(5))

    Octant(Location(0, 0), Location(0, -2)) should ===(Octant(6))
    Octant(Location(0, 0), Location(1, -2)) should ===(Octant(6))
    Octant(Location(0, 0), Location(2, -2)) should ===(Octant(7))
    Octant(Location(0, 0), Location(2, -1)) should ===(Octant(7))

    Octant(Location(5, 5), Location(5, 5)) should ===(Octant(0))
    Octant(Location(5, 5), Location(6, 7)) should ===(Octant(1))
    Octant(Location(5, 5), Location(3, 4)) should ===(Octant(4))
  }

  it should "normalize and denormalize location" in {
    val a = Location(0, 0)
    val b = Location(-5, 1)
    Octant(a, b).normalize(b) should ===(Location(5, 1))

    val random = new Random()

    for (i <- 0 to 100) {
      val a = generateLocation(random)
      val b = generateLocation(random)
      val octant = Octant(a, b)
      octant.denormalize(octant.normalize(a)) should ===(a)
      octant.denormalize(octant.normalize(b)) should ===(b)
    }
  }

  def generateLocation(random: Random): Location = {
    val x = random.nextInt(10) - 5
    val y = random.nextInt(10) - 5

    Location(x, y)
  }
}
