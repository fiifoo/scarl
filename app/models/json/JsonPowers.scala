package models.json

import io.github.fiifoo.scarl.core.power.Powers
import play.api.libs.json._

object JsonPowers {
  lazy private implicit val creaturePowerMapReads = JsonCreaturePower.creaturePowerMapReads
  lazy private implicit val itemPowerMapReads = JsonItemPower.itemPowerMapReads

  lazy val powersReads: Reads[Powers] = Json.reads[Powers]
}
