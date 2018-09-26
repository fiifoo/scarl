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
      factions = state.game.world.assets.factions.values,
      kinds = state.game.world.assets.kinds,
      recipes = state.game.world.assets.recipes.values,
      settings = state.game.settings,

      equipments = state.instance.equipments.getOrElse(state.game.player, Map()),
      inventory = getContainerItems(state.instance)(state.game.player) map (_ (state.instance)),
      playerRecipes = state.instance.recipes.getOrElse(state.game.player, Set()),
      recycledItems = state.instance.creature.recycledItems.getOrElse(state.game.player, List())
    )

    state.copy(
      outMessages = message :: state.outMessages
    )
  }
}
