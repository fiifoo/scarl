package models.json

import io.github.fiifoo.scarl.world.system.source.{SolarSystemSource, SpaceshipSourceId, StellarBodySourceId}
import play.api.libs.json._

object JsonSolarSystemSource {

  import JsonBase.mapFormat

  lazy private implicit val spaceshipSourceIdFormat = JsonSpaceshipSource.spaceshipSourceIdFormat
  lazy private implicit val stellarBodySourceIdFormat = JsonStellarBodySource.stellarBodySourceIdFormat
  implicitly(mapFormat[SpaceshipSourceId, StellarBodySourceId])

  lazy implicit val solarSystemSourceOrbiterFormat: Format[SolarSystemSource.Orbiter] = Json.format
  lazy implicit val solarSystemSourceVisitorFormat: Format[SolarSystemSource.Visitor] = Json.format

  lazy implicit val solarSystemSourceFormat: Format[SolarSystemSource] = Json.format
}
