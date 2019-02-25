package io.github.fiifoo.scarl.world.system.source

import io.github.fiifoo.scarl.world.system.Coordinates.Quarter
import io.github.fiifoo.scarl.world.system._
import io.github.fiifoo.scarl.world.system.source.SolarSystemSource.{Orbiter, Visitor}

import scala.util.Random

object SolarSystemSource {

  case class Orbiter(body: StellarBodySourceId, distance: Double)

  case class Visitor(body: StellarBodySourceId,
                     target1: StellarBodySourceId,
                     target2: StellarBodySourceId,
                     distance: Double,
                     speed: Double
                    )

}

case class SolarSystemSource(sun: StellarBodySourceId,
                             companion: Option[Orbiter],
                             visitors: List[Visitor],
                             ships: Map[SpaceshipSourceId, StellarBodySourceId],
                            ) {

  type BodySources = Map[StellarBodySourceId, StellarBodySource]

  def apply(bodies: Map[StellarBodySourceId, StellarBodySource],
            ships: Map[SpaceshipSourceId, SpaceshipSource],
            random: Random,
           ): SolarSystem = {

    val bodyResults = calculateBodies(bodies, random)
    val shipResults = calculateShips(ships, bodyResults)

    SolarSystem(
      bodyResults,
      shipResults
    )
  }

  private def calculateBodies(bodies: BodySources, random: Random): Map[StellarBody.Id, StellarBody] = {
    val system = this.companion map (companion => {
      this.calculateBinaryOrbit(bodies, random)(this.sun, companion) flatMap (calculateBody(bodies, random) _).tupled
    }) getOrElse {
      calculateBody(bodies, random)(bodies(this.sun))
    }

    val visitors = this.visitors flatMap calculateVisitor(bodies, system, random)

    ((system ::: visitors) map (x => x.id -> x)).toMap
  }

  private def calculateVisitor(bodies: BodySources, system: List[StellarBody], random: Random)
                              (visitor: Visitor): List[StellarBody] = {
    system find (_.source == visitor.target1) flatMap (t1 => {
      system find (_.source == visitor.target2) map (t2 => {
        val quarter = Quarter(t1.position, t2.position)
        val p1 = quarter.normalize(t1.position)
        val p2 = quarter.normalize(t2.position)
        val dx = p2.x - p1.x
        val dy = p2.y - p1.y

        val position = quarter.normalize(p1.add(Physics.vector(dy / dx, visitor.distance * Physics.AU).flip))
        val speed = quarter.normalize(Physics.vector(dy / dx, visitor.speed * Physics.LightSpeed))

        this.calculateBody(bodies, random)(bodies(visitor.body), position, speed)
      })
    }) getOrElse {
      List()
    }
  }

  private def calculateBody(bodies: BodySources,
                            random: Random
                           )(source: StellarBodySource,
                             position: Position = Position(0, 0),
                             speed: Vector = Vector(0, 0)
                           ): List[StellarBody] = {

    val body = StellarBody(
      id = source.id.value,
      source = source.id,
      mass = source.category.mass(source.mass),
      position = position,
      speed = speed
    )

    body :: calculateOrbiters(bodies, random)(body)(source.orbiters)
  }

  private def calculateOrbiters(bodies: BodySources, random: Random)
                               (central: StellarBody)
                               (orbiters: List[Orbiter]): List[StellarBody] = {
    orbiters flatMap this.calculateOrbiter(bodies, random)(central)
  }

  private def calculateOrbiter(bodies: BodySources, random: Random)
                              (central: StellarBody)
                              (orbiter: Orbiter): List[StellarBody] = {
    val source = bodies(orbiter.body)
    val (position, speed) = calculateOrbit(random)(central)(orbiter.distance * Physics.AU)

    this.calculateBody(bodies, random)(source, position, speed)
  }

  private def calculateBinaryOrbit(bodies: BodySources, random: Random)
                                  (primary: StellarBodySourceId, companion: Orbiter): List[(StellarBodySource, Position, Vector)] = {
    val a = bodies(primary)
    val b = bodies(companion.body)

    val m1 = a.category.mass(a.mass)
    val m2 = b.category.mass(b.mass)
    val distance = companion.distance * Physics.AU

    val ((r1, v1), (r2, v2)) = Physics.binarySystem(m1, m2, distance)

    List(
      (a, Position(r1, 0), Vector(0, -v1)),
      (b, Position(-r2, 0), Vector(0, v2))
    )
  }

  private def calculateOrbit(random: Random)
                            (central: StellarBody)
                            (distance: Double): (Position, Vector) = {
    val x = distance * random.nextDouble()
    val y = math.sqrt(math.pow(distance, 2) - math.pow(x, 2))

    val position = Position(x, y)
    val speed = Physics.vector(-1 / (y / x), Physics.orbitalSpeed(central.mass, distance))

    val quarter = Quarter(random.nextInt(4))

    (
      quarter.normalize(position).add(central.position),
      quarter.rotate(speed).add(central.speed)
    )
  }

  private def calculateShips(ships: Map[SpaceshipSourceId, SpaceshipSource],
                             bodyResults: Map[StellarBody.Id, StellarBody],
                            ): Map[Spaceship.Id, Spaceship] = {
    val data = this.ships flatMap { case (shipId, portId) =>
      ships.get(shipId) flatMap (ship => {
        bodyResults.get(portId.value) map (port => {
          shipId -> (ship, port)
        })
      })
    }

    data map { case (_, (source, port)) =>
      val ship = Spaceship(
        id = source.id.value,
        source = source.id,
        acceleration = source.acceleration * Physics.g,
        port = Some(port.id)
      )

      ship.id -> ship
    }
  }
}
