package models.json

import io.github.fiifoo.scarl.world.system.{Position, StellarBody, Vector}
import play.api.libs.json._

object JsonStellarBody {
  lazy private implicit val stellarBodySourceIdFormat = JsonStellarBodySource.stellarBodySourceIdFormat

  lazy private implicit val positionFormat = Json.format[Position]
  lazy private implicit val vectorFormat = Json.format[Vector]

  lazy implicit val stellarBodyFormat: Format[StellarBody] = Json.format
}
