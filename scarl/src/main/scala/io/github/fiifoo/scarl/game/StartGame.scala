package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.core.entity.Selectors.getContainerItems
import io.github.fiifoo.scarl.game.api._
import io.github.fiifoo.scarl.game.area.AreaInfo
import io.github.fiifoo.scarl.game.player.PlayerInfo

object StartGame {

  def apply(gameState: GameState): RunState = {
    val instance = gameState.world.states(gameState.area)

    var state = RunState(
      areaMap = gameState.maps.getOrElse(gameState.area, Map()),
      game = gameState,
      instance = instance,
      playerInfo = PlayerInfo(instance, gameState.player),
      statistics = gameState.statistics
    )

    state = send(state)
    state = RunGame(state)

    state
  }

  private def send(state: RunState): RunState = {
    val message = GameStart(state)

    state.copy(
      outMessages = message :: state.outMessages
    )
  }
}
