package io.github.fiifoo.scarl.game

object EndGame {

  def apply(state: RunState): GameState = {
    val maps = state.game.maps
    val world = state.game.world
    val area = state.game.area

    state.game.copy(
      maps = maps + (area -> state.areaMap),
      statistics = state.statistics,
      world = world.copy(
        states = world.states + (area -> state.instance)
      ))
  }
}
