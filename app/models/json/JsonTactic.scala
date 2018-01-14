package models.json

import io.github.fiifoo.scarl.ai.tactic._
import io.github.fiifoo.scarl.core.ai.{Behavior, Tactic}
import io.github.fiifoo.scarl.core.geometry.Location
import play.api.libs.json._

object JsonTactic {

  import JsonBase.polymorphicTypeFormat

  lazy private implicit val charFormat = JsonBase.charFormat
  lazy private implicit val creatureIdFormat = JsonCreature.creatureIdFormat
  lazy private implicit val locationFormat = Json.format[Location]
  lazy private implicit val safeCreatureIdFormat = JsonCreature.safeCreatureIdFormat

  lazy private implicit val chargeFormat = Json.format[ChargeTactic]
  lazy private implicit val followFormat = Json.format[FollowTactic]
  lazy private implicit val missileFormat = Json.format[MissileTactic]
  lazy private implicit val pursueFormat = Json.format[PursueTactic]
  lazy private implicit val travelFormat = Json.format[TravelTactic]

  lazy implicit val tacticFormat: Format[Tactic] = polymorphicTypeFormat(
    data => {
      case "GreetTactic" => GreetTactic
      case "FollowerTactic" => FollowerTactic
      case "PassTactic" => PassTactic
      case "RoamTactic" => RoamTactic

      case "ChargeTactic" => data.as[ChargeTactic]
      case "FollowTactic" => data.as[FollowTactic]
      case "MissileTactic" => data.as[MissileTactic]
      case "PursueTactic" => data.as[PursueTactic]
      case "TravelTactic" => data.as[TravelTactic]
    }, {
      case GreetTactic => JsNull
      case FollowerTactic => JsNull
      case PassTactic => JsNull
      case RoamTactic => JsNull

      case tactic: ChargeTactic => chargeFormat.writes(tactic)
      case tactic: FollowTactic => followFormat.writes(tactic)
      case tactic: MissileTactic => missileFormat.writes(tactic)
      case tactic: PursueTactic => pursueFormat.writes(tactic)
      case tactic: TravelTactic => travelFormat.writes(tactic)
    }
  )

  lazy val behaviorFormat: Format[Behavior] = new Format[Behavior] {
    override def writes(o: Behavior): JsValue = tacticFormat.writes(o)

    override def reads(json: JsValue): JsResult[Behavior] = tacticFormat.reads(json) map (_.asInstanceOf[Behavior])
  }
}
