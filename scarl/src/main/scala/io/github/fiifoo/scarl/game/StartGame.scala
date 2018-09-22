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
    val message = GameStart(
      area = AreaInfo(state),
      equipments = state.instance.equipments.getOrElse(state.game.player, Map()),
      factions = state.game.world.assets.factions.values,
      inventory = getContainerItems(state.instance)(state.game.player) map (_ (state.instance)),
      kinds = state.game.world.assets.kinds,
      playerRecipes = state.instance.recipes.getOrElse(state.game.player, Set()),
      recipes = state.game.world.assets.recipes.values,
      settings = state.game.settings
    )

    state.copy(
      outMessages = message :: state.outMessages
    )
  }
}
