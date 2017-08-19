package game.json

import game.json.FormatBase._
import game.json.FormatId._
import io.github.fiifoo.scarl.ai.tactic._
import io.github.fiifoo.scarl.core.action.{Behavior, PassTactic, Tactic}
import play.api.libs.json._

object FormatTactic {
  private implicit val formatChargeTactic = Json.format[ChargeTactic]
  private implicit val formatFollowTactic = Json.format[FollowTactic]
  private implicit val formatMissileTactic = Json.format[MissileTactic]
  private implicit val formatPursueTactic = Json.format[PursueTactic]

  implicit val formatTactic = new Format[Tactic] {
    def writes(tactic: Tactic) = JsObject(Map(
      "type" -> JsString(tacticName(tactic)),
      "value" -> (tactic match {
        case FollowerTactic => JsNull
        case PassTactic => JsNull
        case RoamTactic => JsNull

        case tactic: ChargeTactic => formatChargeTactic.writes(tactic)
        case tactic: FollowTactic => formatFollowTactic.writes(tactic)
        case tactic: MissileTactic => formatMissileTactic.writes(tactic)
        case tactic: PursueTactic => formatPursueTactic.writes(tactic)
      })
    ))

    def reads(json: JsValue) = {
      val obj = json.as[JsObject].value
      val value = obj("value")

      val tactic = obj("type").as[String] match {
        case "FollowerTactic" => FollowerTactic
        case "PassTactic" => PassTactic
        case "RoamTactic" => RoamTactic

        case "ChargeTactic" => value.as[ChargeTactic]
        case "FollowTactic" => value.as[FollowTactic]
        case "MissileTactic" => value.as[MissileTactic]
        case "PursueTactic" => value.as[PursueTactic]
      }

      JsSuccess(tactic)
    }
  }

  implicit val formatBehavior = new Format[Behavior] {
    def writes(behavior: Behavior): JsValue = formatTactic.writes(behavior)

    def reads(json: JsValue): JsResult[Behavior] = formatTactic.reads(json) map (_.asInstanceOf[Behavior])
  }

  private def tacticName(tactic: Tactic): String = {
    tactic.getClass.getSimpleName.replace("$", "")
  }
}
