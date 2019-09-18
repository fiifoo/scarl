package models.json

import io.github.fiifoo.scarl.game.api.PlayerInfo
import play.api.libs.json._

object JsonPlayerInfo {
  lazy private implicit val communicationIdFormat = JsonCommunication.communicationIdFormat
  lazy private implicit val creatureInfoFormat = JsonCreature.creatureInfoFormat
  lazy private implicit val creatureStatsFormat = JsonCreatureStats.creatureStatsFormat
  lazy private implicit val itemFormat = JsonItem.itemFormat
  lazy private implicit val itemIdFormat = JsonItem.itemIdFormat
  lazy private implicit val itemKindIdFormat = JsonItemKind.itemKindIdFormat
  lazy private implicit val keyFormat = JsonItemKey.keyFormat
  lazy private implicit val recipeIdFormat = JsonRecipe.recipeIdFormat
  lazy private implicit val slotFormat = JsonItemEquipment.slotFormat

  lazy val playerInfoWrites: Writes[PlayerInfo] = Json.writes[PlayerInfo]
}
