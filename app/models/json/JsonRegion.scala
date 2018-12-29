package models.json

import io.github.fiifoo.scarl.world.{Region, RegionId, SiteId, TransportCategory}
import play.api.libs.json._

object JsonRegion {

  import JsonBase.{mapReads, stringIdFormat}

  lazy private implicit val siteIdFormat = JsonSite.siteIdFormat
  lazy private implicit val transportCategoryFormat = JsonTransportCategory.transportCategoryFormat
  lazy private implicit val variantReads = JsonRegionVariant.variantReads
  lazy private implicit val worldIdFormat = JsonWorld.worldIdFormat

  implicitly(mapReads[TransportCategory, Set[SiteId]])

  lazy implicit val regionIdFormat: Format[RegionId] = stringIdFormat(_.value, RegionId.apply)

  lazy implicit val regionReads: Reads[Region] = Json.reads

  lazy val regionMapReads: Reads[Map[RegionId, Region]] = mapReads
}
