package models.json

import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId, SafeCreatureId}
import io.github.fiifoo.scarl.core.geometry.Location
import play.api.libs.json._

object JsonCreature {

  import JsonBase.integerIdFormat

  lazy private implicit val behaviorFormat = JsonTactic.behaviorFormat
  lazy private implicit val creatureCharacterFormat = JsonCreatureCharacter.creatureCharacterFormat
  lazy private implicit val creatureKindIdFormat = JsonCreatureKind.creatureKindIdFormat
  lazy private implicit val creatureMissileFormat = JsonCreatureMissile.creatureMissileFormat
  lazy private implicit val creaturePartyFormat = JsonCreatureParty.creaturePartyFormat
  lazy private implicit val creaturePowerFormat = JsonCreaturePower.creaturePowerFormat
  lazy private implicit val creatureStatsFormat = JsonCreatureStats.creatureStatsFormat
  lazy private implicit val factionIdFormat = JsonFaction.factionIdFormat
  lazy private implicit val locationFormat = Json.format[Location]
  lazy private implicit val lockFormat = JsonItemLock.lockFormat

  lazy implicit val creatureIdFormat: Format[CreatureId] = integerIdFormat(_.value, CreatureId.apply)

  lazy implicit val safeCreatureIdFormat: Format[SafeCreatureId] = new Format[SafeCreatureId] {
    def reads(json: JsValue): JsResult[SafeCreatureId] = JsSuccess(SafeCreatureId(json.as[Int]))

    def writes(o: SafeCreatureId): JsValue = JsNumber(o.value)
  }

  lazy val creatureFormat: Format[Creature] = Json.format[Creature]
}
