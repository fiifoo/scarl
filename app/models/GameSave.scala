package models

import game.json.FormatGameState._
import io.github.fiifoo.scarl.game.GameState
import play.api.libs.json.Json

case class GameSave(state: GameState,
                    checkHashCode: Int
                   )

object GameSave {
  val format = Json.format[GameSave]
}
