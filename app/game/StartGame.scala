package game

import io.github.fiifoo.scarl.game.{Game, RunState}
import io.github.fiifoo.scarl.world.WorldAssets
import play.api.libs.json.JsValue

object StartGame {

  def apply(assets: WorldAssets, save: Option[JsValue]): RunState = {
    Game.start(save map LoadGame(assets) getOrElse GenerateGame(assets))
  }
}
