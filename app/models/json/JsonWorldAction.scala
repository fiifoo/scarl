package models.json

import io.github.fiifoo.scarl.game.WorldAction
import io.github.fiifoo.scarl.game.WorldAction.{Disembark, Embark, Travel}
import play.api.libs.json._

object JsonWorldAction {

  import JsonBase.polymorphicTypeReads

  lazy private implicit val regionIdFormat = JsonRegion.regionIdFormat
  lazy private implicit val siteIdFormat = JsonSite.siteIdFormat
  lazy private implicit val transportIdFormat = JsonTransport.transportIdFormat

  lazy private implicit val disembarkReads = Json.reads[Disembark]
  lazy private implicit val embarkReads = Json.reads[Embark]
  lazy private implicit val travelReads = Json.reads[Travel]

  lazy val worldActionReads: Reads[WorldAction] = polymorphicTypeReads(data => {
    case "Disembark" => data.as[Disembark]
    case "Embark" => data.as[Embark]
    case "Travel" => data.as[Travel]
  })
}
