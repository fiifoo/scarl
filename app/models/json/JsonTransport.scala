package models.json

import io.github.fiifoo.scarl.world._
import play.api.libs.json._

object JsonTransport {

  import JsonBase.{mapReads, stringIdFormat}

  lazy private implicit val regionIdFormat = JsonRegion.regionIdFormat
  lazy private implicit val siteIdFormat = JsonSite.siteIdFormat
  lazy private implicit val transportCategoryFormat = JsonTransportCategory.transportCategoryFormat

  lazy implicit val transportIdFormat: Format[TransportId] = stringIdFormat(_.value, TransportId.apply)

  lazy implicit val transportReads: Reads[Transport] = Json.reads

  lazy val transportMapReads: Reads[Map[TransportId, Transport]] = mapReads
}
