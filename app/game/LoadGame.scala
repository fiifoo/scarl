package game

import io.github.fiifoo.scarl.game.{GameState, LoadGame => Load}
import io.github.fiifoo.scarl.world.WorldAssets
import models.GameSave
import play.api.libs.json.JsValue

object LoadGame {

  def apply(assets: WorldAssets)(json: JsValue): GameState = {
    val data = GameSave.format.reads(json).get

    val gameState = Load(data.state.copy(world = data.state.world.copy(
      assets = assets
    )))

    if (data.checkHashCode != gameState.hashCode) {
      throw new Exception("Corrupt save data.")
    }

    gameState
  }
}
