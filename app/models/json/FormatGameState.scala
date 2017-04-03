package models.json

import io.github.fiifoo.scarl.game.GameState
import models.json.FormatId._
import models.json.FormatWorldState._
import play.api.libs.json._

object FormatGameState {
  implicit val formatGameState = Json.format[GameState]
}
