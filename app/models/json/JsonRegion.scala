package models.json

import io.github.fiifoo.scarl.world.{Region, RegionId}
import play.api.libs.json._

object JsonRegion {

  import JsonBase.{mapReads, stringIdFormat}

  lazy private implicit val worldIdFormat = JsonWorld.worldIdFormat
  lazy private implicit val variantReads = JsonRegionVariant.variantReads

  lazy implicit val regionIdFormat: Format[RegionId] = stringIdFormat(_.value, RegionId.apply)

  lazy implicit val regionReads: Reads[Region] = Json.reads

  lazy val regionMapReads: Reads[Map[RegionId, Region]] = mapReads
}
