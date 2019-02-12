package models.json

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.world.{RegionVariantKey, Site, SiteId}
import play.api.libs.json._

object JsonSite {

  import JsonBase.{mapFormat, mapReads, stringIdFormat}

  lazy private implicit val areaIdFormat = JsonArea.areaIdFormat
  lazy private implicit val regionIdFormat = JsonRegion.regionIdFormat
  lazy private implicit val variantKeyFormat = JsonRegionVariant.variantKeyFormat

  implicitly(mapFormat[RegionVariantKey, Set[AreaId]])

  lazy implicit val siteIdFormat: Format[SiteId] = stringIdFormat(_.value, SiteId.apply)

  lazy implicit val siteFormat: Format[Site] = Json.format

  lazy val siteMapReads: Reads[Map[SiteId, Site]] = mapReads
}
