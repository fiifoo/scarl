package models.message

import models.Game
import play.api.libs.json._

sealed trait OutMessage

case class Games(games: Seq[Game]) extends OutMessage

case class CreateGameFailed(games: Seq[Game]) extends OutMessage

object OutMessage {
  private implicit val gameWrites = Game.writesWithoutSave

  private val gamesWrites = Json.writes[Games]
  private val createGameFailedWrites = Json.writes[CreateGameFailed]

  val writes = new Writes[OutMessage] {
    def writes(message: OutMessage): JsValue = {
      val data = message match {
        case message: Games => gamesWrites.writes(message)
        case message: CreateGameFailed => createGameFailedWrites.writes(message)
      }

      JsObject(Map(
        "type" -> JsString(message.getClass.getSimpleName),
        "data" -> data
      ))
    }
  }
}
