package models.json

import io.github.fiifoo.scarl.core.world.ConduitId
import io.github.fiifoo.scarl.world.Conduit
import play.api.libs.json._

object JsonConduit {

  import JsonBase.integerIdFormat

  lazy private implicit val itemKindIdFormat = JsonItemKind.itemKindIdFormat
  lazy private implicit val siteIdFormat = JsonSite.siteIdFormat

  lazy implicit val conduitIdFormat: Format[ConduitId] = integerIdFormat(_.value, ConduitId.apply)

  lazy val conduitFormat: Format[Conduit] = Json.format[Conduit]

  lazy val conduitSourceFormat: Format[Conduit.Source] = Json.format[Conduit.Source]
}
