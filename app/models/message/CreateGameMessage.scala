package models.message

import play.api.libs.json._

object CreateGameMessage {
  private implicit val createNewGameReads = Json.reads[CreateNewGame]
  private implicit val createExistingGameReads = Json.reads[CreateExistingGame]

  val reads = new Reads[CreateGameMessage] {
    def reads(json: JsValue): JsResult[CreateGameMessage] = {
      val container = json.as[JsObject].value
      val messageType = container("type").as[String]
      val data = container("data")

      JsSuccess(messageType match {
        case "CreateNewGame" => data.as[CreateNewGame]
        case "CreateExistingGame" => data.as[CreateExistingGame]
      })
    }
  }
}

sealed trait CreateGameMessage

case class CreateNewGame(player: String) extends CreateGameMessage

case class CreateExistingGame(game: Long) extends CreateGameMessage
