package game

import io.github.fiifoo.scarl.game.GameState
import models.GameSave
import play.api.libs.json.JsValue

object SaveGame {

  def apply(state: GameState): JsValue = {
    val data = GameSave(state, state.hashCode)

    GameSave.format.writes(data)
  }
}
