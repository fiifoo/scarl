package game

import io.github.fiifoo.scarl.game.{Game, RunState}
import play.api.libs.json.JsValue

object StartGame {

  def apply(save: Option[JsValue]): RunState = {
    val assets = Data()

    Game.start(save map LoadGame(assets) getOrElse GenerateGame(assets))
  }
}
