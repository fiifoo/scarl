package models

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.mutation.index.NewEntityIndexMutation
import io.github.fiifoo.scarl.world.{WorldManager, WorldState}

object GameUtils {

  def finalizeLoadedWorld(world: WorldState, manager: WorldManager): WorldState = {
    world.copy(
      areas = manager.areas,
      states = world.states.map(x => {
        val (area, state) = x
        val finalized = state.copy(
          index = calculateStateIndex(state),
          kinds = manager.kinds,
          progressions = manager.progressions
        )

        (area, finalized)
      })
    )
  }

  private def calculateStateIndex(s: State): State.Index = {
    s.entities.values.foldLeft(s.index)((index, entity) => {
      NewEntityIndexMutation(entity)(s, index)
    })
  }
}
