package models.message

import models.Game
import play.api.libs.json._

sealed trait OutMessage

case class Games(games: Seq[Game]) extends OutMessage

case class CreateGameFailed(games: Seq[Game]) extends OutMessage

object OutMessage {

  def write(messages: List[OutMessage]): JsValue = {
    implicit val writes = this.writes

    Json.toJson(messages)
  }

  def write(message: OutMessage): JsValue = {
    this.writes.writes(message)
  }

  import models.json.JsonBase.polymorphicTypeWrites

  lazy private implicit val gameWrites = Game.writesWithoutSave

  lazy private val gamesWrites = Json.writes[Games]
  lazy private val createGameFailedWrites = Json.writes[CreateGameFailed]

  lazy val writes: Writes[OutMessage] = polymorphicTypeWrites({
    case message: Games => gamesWrites.writes(message)
    case message: CreateGameFailed => createGameFailedWrites.writes(message)
  })
}
