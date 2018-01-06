package game

import io.github.fiifoo.scarl.game.{EndGame, RunState}
import models.GameSave
import play.api.libs.json.JsValue

object SaveGame {

  def apply(state: RunState): JsValue = {
    val gameState = EndGame(state)
    val data = GameSave(gameState, gameState.hashCode)

    GameSave.format.writes(data)
  }
}
