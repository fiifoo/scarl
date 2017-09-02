package models.message

import play.api.libs.json._

object CreateGameMessage {

  import models.json.JsonBase.polymorphicTypeReads

  lazy private implicit val createNewGameReads = Json.reads[CreateNewGame]
  lazy private implicit val createExistingGameReads = Json.reads[CreateExistingGame]

  lazy val reads: Reads[CreateGameMessage] = polymorphicTypeReads(data => {
    case "CreateNewGame" => data.as[CreateNewGame]
    case "CreateExistingGame" => data.as[CreateExistingGame]
  })
}

sealed trait CreateGameMessage

case class CreateNewGame(player: String) extends CreateGameMessage

case class CreateExistingGame(game: Long) extends CreateGameMessage
