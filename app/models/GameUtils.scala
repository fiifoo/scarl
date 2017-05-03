package models

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.mutation.index.{ConduitLocationIndexAddMutation, EquipmentStatsIndexMutation, NewEntityIndexMutation}
import io.github.fiifoo.scarl.world.{WorldManager, WorldState}

object GameUtils {

  def finalizeLoadedWorld(world: WorldState, manager: WorldManager): WorldState = {
    world.copy(
      areas = manager.areas,
      states = world.states.map(x => {
        val (area, state) = x
        val finalized = state.copy(
          communications = state.communications.copy(manager.communications),
          index = calculateStateIndex(state),
          kinds = manager.kinds,
          progressions = manager.progressions
        )

        (area, finalized)
      })
    )
  }

  private def calculateStateIndex(s: State): State.Index = {
    var index = s.index

    index = s.entities.values.foldLeft(index)((index, entity) => {
      NewEntityIndexMutation(entity)(s, index)
    })
    index = s.conduits.foldLeft(index)((index, x) => {
      val (conduit, location) = x

      index.copy(
        locationConduit = ConduitLocationIndexAddMutation(conduit, location)(index.locationConduit)
      )
    })

    val equipmentStats = s.equipments.keys.foldLeft(index.equipmentStats)((stats, creature) => {
      EquipmentStatsIndexMutation(creature)(s, stats)
    })

    index.copy(equipmentStats = equipmentStats)
  }
}
