package models.json

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.world.{RegionVariantKey, Site, SiteId}
import play.api.libs.json._

object JsonSite {

  import JsonBase.{mapReads, stringIdFormat}

  lazy private implicit val areaIdFormat = JsonArea.areaIdFormat
  lazy private implicit val regionIdFormat = JsonRegion.regionIdFormat
  lazy private implicit val variantKeyFormat = JsonRegionVariant.variantKeyFormat

  lazy private implicit val levelMapReads: Reads[Map[RegionVariantKey, AreaId]] = mapReads

  lazy implicit val siteIdFormat: Format[SiteId] = stringIdFormat(_.value, SiteId.apply)

  lazy implicit val siteReads: Reads[Site] = Json.reads

  lazy val siteMapReads: Reads[Map[SiteId, Site]] = mapReads
}
