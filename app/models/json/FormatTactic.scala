package models.json

import io.github.fiifoo.scarl.ai.tactic.{ChargeTactic, PursueTactic, RoamTactic}
import io.github.fiifoo.scarl.core.action.Tactic
import models.json.FormatBase._
import models.json.FormatId._
import play.api.libs.json._

object FormatTactic {
  implicit val formatChargeTactic = Json.format[ChargeTactic]
  implicit val formatPursueTactic = Json.format[PursueTactic]
  implicit val formatRoamTactic = Json.format[RoamTactic]

  implicit val formatTactic = new Format[Tactic] {
    def writes(tactic: Tactic) = JsObject(Map(
      "type" -> JsString(tactic.getClass.getSimpleName),
      "value" -> (tactic match {
        case tactic: RoamTactic => Json.toJson(tactic)
        case tactic: PursueTactic => Json.toJson(tactic)
        case tactic: ChargeTactic => Json.toJson(tactic)
      })
    ))

    def reads(json: JsValue) = {
      val obj = json.as[JsObject].value
      val value = obj("value")

      val tactic = obj("type").as[String] match {
        case "RoamTactic" => value.as[RoamTactic]
        case "PursueTactic" => value.as[PursueTactic]
        case "ChargeTactic" => value.as[ChargeTactic]
      }

      JsSuccess(tactic)
    }
  }
}
