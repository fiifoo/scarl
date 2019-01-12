package models.json

import io.github.fiifoo.scarl.world.system.{SolarSystem, Spaceship, StellarBody}
import play.api.libs.json._

object JsonSolarSystem {

  import JsonBase.mapFormat

  lazy private implicit val spaceshipFormat = JsonSpaceship.spaceshipFormat
  lazy private implicit val stellarBodyFormat = JsonStellarBody.stellarBodyFormat

  implicitly(mapFormat[StellarBody.Id, StellarBody])
  implicitly(mapFormat[Spaceship.Id, Spaceship])

  lazy implicit val solarSystemFormat: Format[SolarSystem] = Json.format
}
