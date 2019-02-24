package io.github.fiifoo.scarl.world.system

import io.github.fiifoo.scarl.world.system.Spaceship.Travel
import io.github.fiifoo.scarl.world.system.source.SpaceshipSourceId

object Spaceship {
  type Id = String

  case class Travel(destination: StellarBody.Id,
                    from: Position,
                    to: Position,
                    position: Position,
                    speed: Vector = Vector(0, 0),
                   ) {
    def distance: Double = Physics.hypotenuse(this.to.x - this.from.x, this.to.y - this.from.y)

    def finished: Boolean = {
      val quarter = Coordinates.Quarter(this.from, this.to)

      // speed stopped or in reverse
      quarter.normalize(this.speed).x <= 0
    }
  }

}

case class Spaceship(id: Spaceship.Id,
                     source: SpaceshipSourceId,
                     acceleration: Double,
                     travel: Option[Travel] = None,
                     port: Option[StellarBody.Id] = None,
                    ) {

  def tick(tick: Int): Spaceship = {
    this.travel map calculateTravel(tick) getOrElse this
  }

  private def calculateTravel(tick: Int)(travel: Travel): Spaceship = {
    val acceleration = this.calculateTravelAcceleration(travel)

    val speed = travel.speed.add(acceleration.multiply(tick))
    val position = travel.position.add(speed.multiply(tick))

    val next = travel.copy(
      position = position,
      speed = speed
    )

    if (next.finished) {
      this.copy(
        travel = None,
        port = Some(travel.destination)
      )
    } else {
      this.copy(
        travel = Some(next),
        port = None
      )
    }
  }

  private def calculateTravelAcceleration(travel: Travel): Vector = {
    val quarter = Coordinates.Quarter(travel.from, travel.to)
    val p1 = quarter.normalize(travel.position)
    val p2 = quarter.normalize(travel.to)

    val dx = p2.x - p1.x
    val dy = p2.y - p1.y

    val distance = Physics.hypotenuse(dx, dy)
    val half = travel.distance / 2
    val a = if (dx < 0 || distance < half) -this.acceleration else this.acceleration

    quarter.normalize(Physics.vector(dy / dx, a))
  }
}
