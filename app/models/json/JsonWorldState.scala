package models.json

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.world.ConduitId
import io.github.fiifoo.scarl.world.{Conduit, RegionId, SiteId, VariantKey, WorldAssets, WorldState}
import play.api.libs.json._

object JsonWorldState {

  import JsonBase.{emptyFormat, mapFormat, optionFormat}

  // reset from game data
  lazy private implicit val assetsFormat = emptyFormat(WorldAssets())

  lazy private implicit val conduitFormat = JsonConduit.conduitFormat
  lazy private implicit val conduitIdFormat = JsonConduit.conduitIdFormat
  lazy private implicit val regionIdFormat = JsonRegion.regionIdFormat
  lazy private implicit val siteIdFormat = JsonSite.siteIdFormat
  lazy private implicit val stateFormat = JsonState.stateFormat
  lazy private implicit val varianKeyFormat = JsonVariant.variantKeyFormat

  implicitly(mapFormat[ConduitId, Conduit])
  implicitly(mapFormat[SiteId, State])
  implicitly(optionFormat[VariantKey])
  implicitly(mapFormat[RegionId, Option[VariantKey]])

  lazy val worldStateFormat = Json.format[WorldState]
}
