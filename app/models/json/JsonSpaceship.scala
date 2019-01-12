package models.json

import io.github.fiifoo.scarl.world.system.{Position, Spaceship, Vector}
import play.api.libs.json._

object JsonSpaceship {
  lazy private implicit val spaceshipSourceIdFormat = JsonSpaceshipSource.spaceshipSourceIdFormat

  lazy private implicit val positionFormat = Json.format[Position]
  lazy private implicit val vectorFormat = Json.format[Vector]
  lazy private implicit val travelFormat = Json.format[Spaceship.Travel]

  lazy implicit val spaceshipFormat: Format[Spaceship] = Json.format
}
