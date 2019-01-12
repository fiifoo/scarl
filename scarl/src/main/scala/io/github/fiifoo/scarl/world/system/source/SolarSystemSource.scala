package io.github.fiifoo.scarl.world.system.source

import io.github.fiifoo.scarl.world.system._

case class SolarSystemSource(suns: List[StellarBodySourceId],
                             ships: Map[SpaceshipSourceId, StellarBodySourceId],
                            ) {

  def apply(bodies: Map[StellarBodySourceId, StellarBodySource],
            ships: Map[SpaceshipSourceId, SpaceshipSource],
           ): SolarSystem = {

    val bodyResults = calculateBodies(bodies)
    val shipResults = calculateShips(ships, bodyResults)

    SolarSystem(
      bodyResults,
      shipResults
    )
  }

  private def calculateBodies(bodies: Map[StellarBodySourceId, StellarBodySource]): Map[StellarBody.Id, StellarBody] = {
    def calculate(orbiters: List[StellarBodySource],
                  orbited: Option[StellarBodySource] = None
                 ): List[StellarBody] = {
      orbiters flatMap (source => {
        val body = StellarBody(
          id = source.id.value,
          source = source.id,
          mass = source.category.mass(source.mass),
          position = Position(source.position * Physics.AU, 0), // todo: other positions
          speed = Vector(0, source.speed) // todo: vector by position
        )

        body :: calculate(source.orbiters flatMap bodies.get, orbited)
      })
    }

    (calculate(this.suns flatMap bodies.get) map (x => x.id -> x)).toMap
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
