package models.json

import io.github.fiifoo.scarl.core.entity.{CreaturePower, ItemPower, Power}
import io.github.fiifoo.scarl.power._
import play.api.libs.json._

object JsonPower {

  import JsonBase.polymorphicTypeFormat

  lazy private implicit val communicationIdFormat = JsonCommunication.communicationIdFormat
  lazy private implicit val conditionFormat = JsonCondition.conditionFormat
  lazy private implicit val dispositionFormat = JsonFaction.dispositionFormat
  lazy private implicit val goalIdFormat = JsonGoal.goalIdFormat
  lazy private implicit val kindIdFormat = JsonKind.kindIdFormat
  lazy private implicit val resourcesFormat = Json.format[Power.Resources]
  lazy private implicit val strategyFormat = JsonStrategy.strategyFormat

  lazy private implicit val achieveGoalFormat = Json.format[AchieveGoalPower]
  lazy private implicit val activateMachineryFormat = Json.format[ActivateMachineryPower]
  lazy private implicit val captureFormat = Json.format[CapturePower]
  lazy private implicit val compositeCreatureFormat = Json.format[CompositeCreaturePower]
  lazy private implicit val compositeItemFormat = Json.format[CompositeItemPower]
  lazy private implicit val createEntityFormat = Json.format[CreateEntityPower]
  lazy private implicit val explodeFormat = Json.format[ExplodePower]
  lazy private implicit val factionDispositionFormat = Json.format[FactionDispositionPower]
  lazy private implicit val factionStrategyFormat = Json.format[FactionStrategyPower]
  lazy private implicit val maybeCreatureFormat = Json.format[MaybeCreaturePower]
  lazy private implicit val maybeItemFormat = Json.format[MaybeItemPower]
  lazy private implicit val receiveCommunicationFormat = Json.format[ReceiveCommunicationPower]
  lazy private implicit val receiveKeyFormat = Json.format[ReceiveKeyPower]
  lazy private implicit val removeItemFormat = Json.format[RemoveItemPower]
  lazy private implicit val scanFormat = Json.format[ScanPower]
  lazy private implicit val transformFormat = Json.format[TransformPower]
  lazy private implicit val trapAttackFormat = Json.format[TrapAttackPower]
  lazy private implicit val voidFormat = Json.format[VoidPower]

  lazy implicit val creaturePowerFormat: Format[CreaturePower] = polymorphicTypeFormat(
    data => {
      case "AchieveGoalPower" => data.as[AchieveGoalPower]
      case "ActivateMachineryPower" => data.as[ActivateMachineryPower]
      case "CapturePower" => data.as[CapturePower]
      case "CompositeCreaturePower" => data.as[CompositeCreaturePower]
      case "CreateEntityPower" => data.as[CreateEntityPower]
      case "ExplodePower" => data.as[ExplodePower]
      case "FactionDispositionPower" => data.as[FactionDispositionPower]
      case "FactionStrategyPower" => data.as[FactionStrategyPower]
      case "MaybeCreaturePower" => data.as[MaybeCreaturePower]
      case "TransformPower" => data.as[TransformPower]
      case "VoidPower" => data.as[VoidPower]
    }, {
      case power: AchieveGoalPower => achieveGoalFormat.writes(power)
      case power: ActivateMachineryPower => activateMachineryFormat.writes(power)
      case power: CapturePower => captureFormat.writes(power)
      case power: CompositeCreaturePower => compositeCreatureFormat.writes(power)
      case power: CreateEntityPower => createEntityFormat.writes(power)
      case power: ExplodePower => explodeFormat.writes(power)
      case power: FactionDispositionPower => factionDispositionFormat.writes(power)
      case power: FactionStrategyPower => factionStrategyFormat.writes(power)
      case power: MaybeCreaturePower => maybeCreatureFormat.writes(power)
      case power: TransformPower => transformFormat.writes(power)
      case power: VoidPower => voidFormat.writes(power)
    }
  )

  lazy implicit val itemPowerFormat: Format[ItemPower] = polymorphicTypeFormat(
    data => {
      case "AchieveGoalPower" => data.as[AchieveGoalPower]
      case "ActivateMachineryPower" => data.as[ActivateMachineryPower]
      case "CompositeItemPower" => data.as[CompositeItemPower]
      case "CreateEntityPower" => data.as[CreateEntityPower]
      case "ExplodePower" => data.as[ExplodePower]
      case "MaybeItemPower" => data.as[MaybeItemPower]
      case "ReceiveCommunicationPower" => data.as[ReceiveCommunicationPower]
      case "ReceiveKeyPower" => data.as[ReceiveKeyPower]
      case "RemoveItemPower" => data.as[RemoveItemPower]
      case "ScanPower" => data.as[ScanPower]
      case "TransformPower" => data.as[TransformPower]
      case "TrapAttackPower" => data.as[TrapAttackPower]
      case "VoidPower" => data.as[VoidPower]
    }, {
      case power: AchieveGoalPower => achieveGoalFormat.writes(power)
      case power: ActivateMachineryPower => activateMachineryFormat.writes(power)
      case power: CompositeItemPower => compositeItemFormat.writes(power)
      case power: CreateEntityPower => createEntityFormat.writes(power)
      case power: ExplodePower => explodeFormat.writes(power)
      case power: MaybeItemPower => maybeItemFormat.writes(power)
      case power: ReceiveCommunicationPower => receiveCommunicationFormat.writes(power)
      case power: ReceiveKeyPower => receiveKeyFormat.writes(power)
      case power: RemoveItemPower => removeItemFormat.writes(power)
      case power: ScanPower => scanFormat.writes(power)
      case power: TransformPower => transformFormat.writes(power)
      case power: TrapAttackPower => trapAttackFormat.writes(power)
      case power: VoidPower => voidFormat.writes(power)
    }
  )
}
