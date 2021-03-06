package models.json

import io.github.fiifoo.scarl.action._
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.entity.ItemId
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item.Equipment.Slot
import play.api.libs.json._

object JsonAction {

  import JsonBase.{mapFormat, polymorphicTypeReads}

  lazy private implicit val communicationIdFormat = JsonCommunication.communicationIdFormat
  lazy private implicit val conduitIdFormat = JsonConduit.conduitIdFormat
  lazy private implicit val creatureIdFormat = JsonCreature.creatureIdFormat
  lazy private implicit val creatureKindIdFormat = JsonCreatureKind.creatureKindIdFormat
  lazy private implicit val itemKindIdFormat = JsonItemKind.itemKindIdFormat
  lazy private implicit val itemIdFormat = JsonItem.itemIdFormat
  lazy private implicit val locationReads = Json.reads[Location]
  lazy private implicit val recipeIdFormat = JsonRecipe.recipeIdFormat
  lazy private implicit val slotFormat = JsonItemEquipment.slotFormat
  lazy private implicit val stanceFormat = JsonStance.stanceFormat

  implicitly(mapFormat[Slot, ItemId])

  lazy private implicit val attackReads = Json.reads[AttackAction]
  lazy private implicit val cancelRecycleItemReads = Json.reads[CancelRecycleItemAction]
  lazy private implicit val changeStanceReads = Json.reads[ChangeStanceAction]
  lazy private implicit val converseReads = {
    implicit val usableIdFormat = JsonEntity.usableIdFormat

    Json.reads[ConverseAction]
  }
  lazy private implicit val communicateReads = Json.reads[CommunicateAction]
  lazy private implicit val craftItemReads = Json.reads[CraftItemAction]
  lazy private implicit val displaceReads = Json.reads[DisplaceAction]
  lazy private implicit val dropItemReads = Json.reads[DropItemAction]
  lazy private implicit val enterConduitReads = Json.reads[EnterConduitAction]
  lazy private implicit val equipItemReads = Json.reads[EquipItemAction]
  lazy private implicit val equipWeaponsReads = Json.reads[EquipWeaponsAction]
  lazy private implicit val hackCreatureReads = Json.reads[HackCreatureAction]
  lazy private implicit val hackItemReads = Json.reads[HackItemAction]
  lazy private implicit val moveReads = Json.reads[MoveAction]
  lazy private implicit val pickItemReads = Json.reads[PickItemAction]
  lazy private implicit val recycleItemReads = Json.reads[RecycleItemAction]
  lazy private implicit val shootReads = Json.reads[ShootAction]
  lazy private implicit val shootMissileReads = Json.reads[ShootMissileAction]
  lazy private implicit val unequipItemReads = Json.reads[UnequipItemAction]
  lazy private implicit val useCreatureReads = Json.reads[UseCreatureAction]
  lazy private implicit val useDoorReads = Json.reads[UseDoorAction]
  lazy private implicit val useItemReads = Json.reads[UseItemAction]

  lazy val actionReads: Reads[Action] = polymorphicTypeReads(data => {
    case "Attack" => data.as[AttackAction]
    case "CancelRecycleItem" => data.as[CancelRecycleItemAction]
    case "ChangeStance" => data.as[ChangeStanceAction]
    case "Communicate" => data.as[CommunicateAction]
    case "Converse" => data.as[ConverseAction]
    case "CraftItem" => data.as[CraftItemAction]
    case "Displace" => data.as[DisplaceAction]
    case "DropItem" => data.as[DropItemAction]
    case "EndConversation" => EndConversationAction
    case "EnterConduit" => data.as[EnterConduitAction]
    case "EquipItem" => data.as[EquipItemAction]
    case "EquipWeapons" => data.as[EquipWeaponsAction]
    case "HackCreature" => data.as[HackCreatureAction]
    case "HackItem" => data.as[HackItemAction]
    case "Move" => data.as[MoveAction]
    case "Pass" => PassAction
    case "PickItem" => data.as[PickItemAction]
    case "RecycleItem" => data.as[RecycleItemAction]
    case "Shoot" => data.as[ShootAction]
    case "ShootMissile" => data.as[ShootMissileAction]
    case "UnequipItem" => data.as[UnequipItemAction]
    case "UseCreature" => data.as[UseCreatureAction]
    case "UseDoor" => data.as[UseDoorAction]
    case "UseItem" => data.as[UseItemAction]
  })
}
