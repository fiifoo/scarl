package models.json

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.area.template.TemplateId
import io.github.fiifoo.scarl.core.character.ProgressionId
import io.github.fiifoo.scarl.core.communication.CommunicationId
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.world.ConduitId
import models.json.FormatUtils._
import play.api.libs.json._

object FormatId {
  implicit val formatAreaId = formatStringId[AreaId](_.value, AreaId.apply)
  implicit val formatCommunicationId = formatStringId[CommunicationId](_.value, CommunicationId.apply)
  implicit val formatConduitId = formatIntId[ConduitId](_.value, ConduitId.apply)
  implicit val formatFactionId = formatStringId[FactionId](_.value, FactionId.apply)
  implicit val formatProgressionId = formatStringId[ProgressionId](_.value, ProgressionId.apply)
  implicit val formatTemplateId = formatStringId[TemplateId](_.value, TemplateId.apply)

  implicit val formatCreatureKindId = formatStringId[CreatureKindId](_.value, CreatureKindId.apply)
  implicit val formatItemKindId = formatStringId[ItemKindId](_.value, ItemKindId.apply)
  implicit val formatTerrainKindId = formatStringId[TerrainKindId](_.value, TerrainKindId.apply)
  implicit val formatWallKindId = formatStringId[WallKindId](_.value, WallKindId.apply)
  implicit val formatWidgetKindId = formatStringId[WidgetKindId](_.value, WidgetKindId.apply)

  implicit val formatActiveStatusId = formatIntId[ActiveStatusId](_.value, ActiveStatusId.apply)
  implicit val formatContainerId = formatIntId[ContainerId](_.value, ContainerId.apply)
  implicit val formatCreatureId = formatIntId[CreatureId](_.value, CreatureId.apply)
  implicit val formatItemId = formatIntId[ItemId](_.value, ItemId.apply)
  implicit val formatPassiveStatusId = formatIntId[PassiveStatusId](_.value, PassiveStatusId.apply)
  implicit val formatTerrainId = formatIntId[TerrainId](_.value, TerrainId.apply)
  implicit val formatTriggerStatusId = formatIntId[TriggerStatusId](_.value, TriggerStatusId.apply)
  implicit val formatWallId = formatIntId[WallId](_.value, WallId.apply)

  implicit val formatSafeCreatureId = formatIntId[SafeCreatureId](_.value, SafeCreatureId.apply)

  implicit val formatEntityId = new Format[EntityId] {
    def writes(id: EntityId) = JsObject(Map(
      "type" -> JsString(id.getClass.getSimpleName),
      "value" -> JsNumber(id.value)
    ))

    def reads(json: JsValue) = {
      val obj = json.as[JsObject].value
      val value = obj("value").as[Int]

      val id = obj("type").as[String] match {
        case "ActiveStatusId" => ActiveStatusId(value)
        case "ContainerId" => ContainerId(value)
        case "CreatureId" => CreatureId(value)
        case "ItemId" => ItemId(value)
        case "PassiveStatusId" => PassiveStatusId(value)
        case "TerrainId" => TerrainId(value)
        case "TriggerStatusId" => TriggerStatusId(value)
        case "WallId" => WallId(value)
      }

      JsSuccess(id)
    }
  }

  implicit val formatActorId: Format[ActorId] = new Format[ActorId] {
    def writes(id: ActorId) = JsObject(Map(
      "type" -> JsString(id.getClass.getSimpleName),
      "value" -> JsNumber(id.value)
    ))

    def reads(json: JsValue) = {
      val obj = json.as[JsObject].value
      val value = obj("value").as[Int]

      val id = obj("type").as[String] match {
        case "ActiveStatusId" => ActiveStatusId(value)
        case "CreatureId" => CreatureId(value)
      }

      JsSuccess(id)
    }
  }
}
