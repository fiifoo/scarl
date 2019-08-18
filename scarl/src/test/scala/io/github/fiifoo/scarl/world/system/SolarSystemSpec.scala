package io.github.fiifoo.scarl.world.system

import io.github.fiifoo.scarl.world.system.source.{SpaceshipSourceId, StellarBodySourceId}
import org.scalatest._

import scala.Ordering.Double.TotalOrdering

class SolarSystemSpec extends FlatSpec with Matchers {

  val month = 3600 * 24 * 30

  val sun = StellarBody(
    id = "sun",
    source = StellarBodySourceId(""),
    mass = Physics.SolarMass,
    position = Position(0, 0),
    speed = Vector(0, 0),
  )

  val earth = StellarBody(
    id = "earth",
    source = StellarBodySourceId(""),
    mass = Physics.EarthMass,
    position = Position(Physics.AU, 0),
    speed = Vector(0, 29780),
  )

  val mars = StellarBody(
    id = "mars",
    source = StellarBodySourceId(""),
    //mass = 6.4185 * math.pow(10, 23),
    mass = 0.108 * Physics.EarthMass,
    position = Position(1.52366231 * Physics.AU, 0),
    speed = Vector(0, 24077),
  )

  val ship = Spaceship(
    id = "ship",
    source = SpaceshipSourceId(""),
    acceleration = Physics.g,
    port = Some(mars.id)
  )

  val bodies = Map(
    sun.id -> sun,
    earth.id -> earth,
    mars.id -> mars,
  )

  val ships = Map(
    ship.id -> ship,
  )

  "SolarSystem" should "move planets in orbit" in {
    val tolerance = 0.01
    val ticks = 0 until month / SolarSystem.Tick
    val system = SolarSystem(bodies)

    (ticks foldLeft system) ((system, _) => {
      val next = system.tick()

      val position = next.bodies(earth.id).position
      val distance = Physics.hypotenuse(position.x, position.y)

      distance should not equal Physics.AU
      distance should be > Physics.AU * (1 - tolerance)
      distance should be < Physics.AU * (1 + tolerance)

      next
    })
  }

  it should "move ship to destination port" in {
    val system = SolarSystem(bodies, ships)

    system.time should ===(0)
    system.ships(ship.id).port should ===(Some(mars.id))

    val result = system.travel(ship.id, earth.id)

    result.isDefined should ===(true)
    result.get.time should be > 0
    result.get.ships(ship.id).port should ===(Some(earth.id))
  }
}
