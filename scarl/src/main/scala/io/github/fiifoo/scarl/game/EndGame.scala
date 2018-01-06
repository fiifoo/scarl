package io.github.fiifoo.scarl.game

object EndGame {

  def apply(state: RunState): GameState = {
    val maps = state.gameState.maps
    val world = state.gameState.world
    val area = state.gameState.area

    state.gameState.copy(
      maps = maps + (area -> state.areaMap),
      statistics = state.statistics,
      world = world.copy(
        states = world.states + (area -> state.instance)
      ))
  }
}
