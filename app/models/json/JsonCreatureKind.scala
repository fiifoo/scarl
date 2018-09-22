package models.json

import io.github.fiifoo.scarl.core.kind.{CreatureKind, CreatureKindId}
import play.api.libs.json._

object JsonCreatureKind {

  import JsonBase.{mapReads, stringIdFormat}

  lazy private implicit val behaviorFormat = JsonTactic.behaviorFormat
  lazy private implicit val charFormat = JsonBase.charFormat
  lazy private implicit val communicationIdFormat = JsonCommunication.communicationIdFormat
  lazy private implicit val creatureCharacterFormat = JsonCreatureCharacter.creatureCharacterFormat
  lazy private implicit val creatureEventsFormat = JsonCreatureEvents.creatureEventsFormat
  lazy private implicit val creatureMissileFormat = JsonCreatureMissile.creatureMissileFormat
  lazy private implicit val creaturePowerFormat = JsonPower.creaturePowerFormat
  lazy private implicit val creatureStatsFormat = JsonCreatureStats.creatureStatsFormat
  lazy private implicit val factionIdFormat = JsonFaction.factionIdFormat
  lazy private implicit val itemKindIdFormat = JsonItemKind.itemKindIdFormat
  lazy private implicit val lockSourceFormat = JsonItemLock.lockSourceFormat
  lazy private implicit val recipeIdFormat = JsonRecipe.recipeIdFormat
  lazy private implicit val slotFormat = JsonItemEquipment.slotFormat

  lazy implicit val creatureKindIdFormat: Format[CreatureKindId] = stringIdFormat(_.value, CreatureKindId.apply)

  lazy implicit val creatureKindFormat: Format[CreatureKind] = Json.format

  lazy val creatureKindMapReads: Reads[Map[CreatureKindId, CreatureKind]] = mapReads
}
