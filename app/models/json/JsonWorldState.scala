package models.json

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.world.ConduitId
import io.github.fiifoo.scarl.world._
import play.api.libs.json._

object JsonWorldState {

  import JsonBase.{emptyFormat, mapFormat, optionFormat}

  // reset from game data
  lazy private implicit val assetsFormat = emptyFormat(WorldAssets())

  lazy private implicit val areaIdFormat = JsonArea.areaIdFormat
  lazy private implicit val conduitFormat = JsonConduit.conduitFormat
  lazy private implicit val conduitIdFormat = JsonConduit.conduitIdFormat
  lazy private implicit val goalIdFormat = JsonGoal.goalIdFormat
  lazy private implicit val regionIdFormat = JsonRegion.regionIdFormat
  lazy private implicit val siteIdFormat = JsonSite.siteIdFormat
  lazy private implicit val solarSystemFormat = JsonSolarSystem.solarSystemFormat
  lazy private implicit val stateFormat = JsonState.stateFormat
  lazy private implicit val templateIdFormat = JsonTemplate.templateIdFormat
  lazy private implicit val transportIdFormat = JsonTransport.transportIdFormat
  lazy private implicit val variantKeyFormat = JsonRegionVariant.variantKeyFormat

  implicitly(mapFormat[ConduitId, Conduit])
  implicitly(mapFormat[SiteId, State])
  implicitly(optionFormat[RegionVariantKey])
  implicitly(mapFormat[TransportId, RegionId])
  implicitly(mapFormat[RegionId, Option[RegionVariantKey]])

  lazy val worldStateFormat = Json.format[WorldState]
}
