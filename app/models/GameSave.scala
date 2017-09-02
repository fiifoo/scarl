package models

import io.github.fiifoo.scarl.game.GameState
import models.json.JsonGameState
import play.api.libs.json.{Format, Json}

case class GameSave(state: GameState,
                    checkHashCode: Int
                   )

object GameSave {
  lazy private implicit val gameStateFormat = JsonGameState.gameStateFormat

  lazy val format: Format[GameSave] = Json.format
}
