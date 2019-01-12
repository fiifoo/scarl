package models.json

import io.github.fiifoo.scarl.world.{Region, RegionId, SiteId, TransportCategory}
import play.api.libs.json._

object JsonRegion {

  import JsonBase.{mapFormat, mapReads, stringIdFormat}

  lazy private implicit val siteIdFormat = JsonSite.siteIdFormat
  lazy private implicit val stellarBodySourceIdFormat = JsonStellarBodySource.stellarBodySourceIdFormat
  lazy private implicit val transportCategoryFormat = JsonTransportCategory.transportCategoryFormat
  lazy private implicit val variantFormat = JsonRegionVariant.variantFormat
  lazy private implicit val worldIdFormat = JsonWorld.worldIdFormat

  implicitly(mapFormat[TransportCategory, Set[SiteId]])

  lazy implicit val regionIdFormat: Format[RegionId] = stringIdFormat(_.value, RegionId.apply)

  lazy implicit val regionFormat: Format[Region] = Json.format

  lazy val regionMapReads: Reads[Map[RegionId, Region]] = mapReads
}
