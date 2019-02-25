package models.json

import io.github.fiifoo.scarl.world.system.source.{StellarBodySource, StellarBodySourceId}
import play.api.libs.json._

object JsonStellarBodySource {

  import JsonBase.{mapReads, polymorphicObjectFormat, stringIdFormat}

  lazy private implicit val solarSystemSourceOrbiterFormat = JsonSolarSystemSource.solarSystemSourceOrbiterFormat

  lazy private implicit val stellarBodySourceCategoryFormat: Format[StellarBodySource.Category] = polymorphicObjectFormat({
    case "StellarBodySource.BlackHoleCategory" => StellarBodySource.BlackHoleCategory
    case "StellarBodySource.PlanetCategory" => StellarBodySource.PlanetCategory
    case "StellarBodySource.SunCategory" => StellarBodySource.SunCategory
  })

  lazy implicit val stellarBodySourceIdFormat: Format[StellarBodySourceId] = stringIdFormat(_.value, StellarBodySourceId.apply)

  lazy implicit val stellarBodySourceFormat: Format[StellarBodySource] = Json.format

  lazy val stellarBodyMapReads: Reads[Map[StellarBodySourceId, StellarBodySource]] = mapReads
}
