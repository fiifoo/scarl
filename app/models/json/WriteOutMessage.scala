package models.json

import io.github.fiifoo.scarl.core.geometry.{Location, WaypointNetwork}
import io.github.fiifoo.scarl.core.kind.Kinds
import io.github.fiifoo.scarl.game.api.{AreaInfo, _}
import play.api.libs.json._

object WriteOutMessage {

  import JsonBase.polymorphicTypeWrites

  def apply(messages: List[OutMessage]): JsValue = {
    implicit val writes = this.outMessageWrites

    Json.toJson(messages)
  }

  def apply(message: OutMessage): JsValue = {
    this.outMessageWrites.writes(message)
  }

  lazy private implicit val locationWrites = Json.writes[Location]
  lazy private implicit val factionFormat = JsonFaction.factionFormat
  lazy private implicit val factionInfoWrites = JsonFactionInfo.factionInfoWrites
  lazy private implicit val eventWrites = JsonEvent.eventWrites
  lazy private implicit val mapLocationMapFormat = JsonMapLocation.mapLocationMapFormat
  lazy private implicit val playerFovWrites = JsonPlayerFov.playerFovWrites
  lazy private implicit val playerInfoWrites = JsonPlayerInfo.playerInfoWrites
  lazy private implicit val playerSettingsFormat = JsonPlayerSettings.playerSettingsFormat
  lazy private implicit val recipeFormat = JsonRecipe.recipeFormat
  lazy private implicit val regionFormat = JsonRegion.regionFormat
  lazy private implicit val regionIdFormat = JsonRegion.regionIdFormat
  lazy private implicit val signalFormat = JsonSignal.signalFormat
  lazy private implicit val siteFormat = JsonSite.siteFormat
  lazy private implicit val siteIdFormat = JsonSite.siteIdFormat
  lazy private implicit val solarSystemFormat = JsonSolarSystem.solarSystemFormat
  lazy private implicit val spaceshipSourceFormat = JsonSpaceshipSource.spaceshipSourceFormat
  lazy private implicit val stellarBodySourceFormat = JsonStellarBodySource.stellarBodySourceFormat
  lazy private implicit val statisticsFormat = JsonStatistics.statisticsFormat
  lazy private implicit val transportFormat = JsonTransport.transportFormat
  lazy private implicit val transportIdFormat = JsonTransport.transportIdFormat
  lazy private implicit val waypointNetworkWrites = Json.writes[WaypointNetwork]

  lazy private implicit val creatureKindFormat = JsonCreatureKind.creatureKindFormat
  lazy private implicit val creatureKindIdFormat = JsonCreatureKind.creatureKindIdFormat
  lazy private implicit val itemKindFormat = JsonItemKind.itemKindFormat
  lazy private implicit val itemKindIdFormat = JsonItemKind.itemKindIdFormat
  lazy private implicit val terrainKindFormat = JsonTerrainKind.terrainKindFormat
  lazy private implicit val terrainKindIdFormat = JsonTerrainKind.terrainKindIdFormat
  lazy private implicit val wallKindFormat = JsonWallKind.wallKindFormat
  lazy private implicit val wallKindIdFormat = JsonWallKind.wallKindIdFormat
  lazy private implicit val widgetKindFormat = JsonWidgetKind.widgetKindFormat
  lazy private implicit val widgetKindIdFormat = JsonWidgetKind.widgetKindIdFormat

  lazy private implicit val kindsWrites = new Writes[Kinds] {
    def writes(k: Kinds) = JsObject(Map(
      "creatures" -> Json.toJson(k.creatures),
      "items" -> Json.toJson(k.items),
      "terrains" -> Json.toJson(k.terrains),
      "walls" -> Json.toJson(k.walls),
      "widgets" -> Json.toJson(k.widgets)
    ))
  }

  lazy private implicit val areaInfoWrites = Json.writes[AreaInfo]

  lazy private val debugFovWrites = Json.writes[DebugFov]
  lazy private val debugWaypointWrites = Json.writes[DebugWaypoint]
  lazy private val gameStartWrites = Json.writes[GameStart]
  lazy private val gameUpdateWrites = Json.writes[GameUpdate]
  lazy private val gameOverWrites = Json.writes[GameOver]
  lazy private val areaChangeWrites = Json.writes[AreaChange]
  lazy private val playerSettingsWrites = Json.writes[PlayerSettings]
  lazy private val signalMapWrites = Json.writes[SignalMap]
  lazy private val worldInfoWrites = Json.writes[WorldInfo]

  lazy private val outMessageWrites: Writes[OutMessage] = polymorphicTypeWrites({
    case message: DebugFov => debugFovWrites.writes(message)
    case message: DebugWaypoint => debugWaypointWrites.writes(message)
    case message: GameStart => gameStartWrites.writes(message)
    case message: GameUpdate => gameUpdateWrites.writes(message)
    case message: GameOver => gameOverWrites.writes(message)
    case message: AreaChange => areaChangeWrites.writes(message)
    case message: PlayerSettings => playerSettingsWrites.writes(message)
    case message: SignalMap => signalMapWrites.writes(message)
    case message: WorldInfo => worldInfoWrites.writes(message)
  })
}
