package io.github.fiifoo.scarl.world.system

import io.github.fiifoo.scarl.world.system.Spaceship.Travel

import scala.annotation.tailrec

object SolarSystem {

  val Tick = 3600 // hour

  val MaxTravelTime = 3600 * 24 * 100

}


case class SolarSystem(bodies: Map[StellarBody.Id, StellarBody] = Map(),
                       ships: Map[Spaceship.Id, Spaceship] = Map(),
                       time: Int = 0,
                      ) {
  def tick(tick: Int = SolarSystem.Tick): SolarSystem = {
    val bodies = this.bodies map { case (id, body) =>
      id -> body.tick(tick, this.bodies.values)
    }
    val ships = this.ships map { case (id, ship) =>
      id -> ship.tick(tick)
    }

    this.copy(
      bodies = bodies,
      ships = ships,
      time = this.time + tick
    )
  }

  def travel(ship: Spaceship.Id, destination: StellarBody.Id, tick: Int = SolarSystem.Tick): Option[SolarSystem] = {
    this.calculateTravel(ship, destination, tick) map (travel => {
      val initial = this.copy(
        ships = this.ships + (ship -> this.ships(ship).copy(
          travel = Some(travel)
        ))
      )

      @tailrec
      def step(system: SolarSystem): SolarSystem = {
        if (system.ships(ship).travel.isDefined) {
          step(system.tick(tick))
        } else {
          system
        }
      }

      step(initial)
    })
  }

  def calculateTravel(ship: Spaceship.Id, destination: StellarBody.Id, tick: Int = SolarSystem.Tick): Option[Travel] = {
    this.ships.get(ship) flatMap (ship => {
      ship.port flatMap this.bodies.get flatMap (port => {
        this.calculateTravelTo(tick, ship, port.position, destination) map (to => {
          Travel(
            destination = destination,
            from = port.position,
            to = to,
            position = port.position
          )
        })
      })
    })
  }

  private def calculateTravelTo(tick: Int, ship: Spaceship, from: Position, destination: StellarBody.Id): Option[Position] = {
    val check = this.checkTravelTo(ship, from) _

    @tailrec
    def seek(bodies: Map[StellarBody.Id, StellarBody], destination: StellarBody, time: Int = 0): Option[Position] = {
      if (time > SolarSystem.MaxTravelTime) {
        None
      } else if (check(destination.position, time)) {
        Some(destination.position)
      } else {
        val next = bodies map { case (id, body) =>
          id -> body.tick(tick, bodies.values)
        }

        seek(next, next(destination.id), time + tick)
      }
    }

    seek(this.bodies, this.bodies(destination))
  }

  private def checkTravelTo(ship: Spaceship, from: Position)(to: Position, time: Int): Boolean = {
    val quarter = Coordinates.Quarter(from, to)
    val p1 = quarter.normalize(from)
    val p2 = quarter.normalize(to)

    val dx = p2.x - p1.x
    val dy = p2.y - p1.y

    val distance = Physics.hypotenuse(dx, dy)
    val travelTime = Physics.travelTime(distance / 2, ship.acceleration) * 2

    travelTime <= time
  }
}
