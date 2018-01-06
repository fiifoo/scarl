package game

import io.github.fiifoo.scarl.game.{RunState, StartGame}
import io.github.fiifoo.scarl.world.WorldAssets
import play.api.libs.json.JsValue

object CreateGame {

  def apply(assets: WorldAssets, save: Option[JsValue]): RunState = {
    StartGame(save map LoadGame(assets) getOrElse GenerateGame(assets))
  }
}
