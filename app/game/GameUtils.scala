package game

import io.github.fiifoo.scarl.core.State.Assets
import io.github.fiifoo.scarl.core.mutation.cache.EquipmentStatsCacheMutation
import io.github.fiifoo.scarl.core.mutation.index.{ConduitLocationIndexAddMutation, NewEntityIndexMutation}
import io.github.fiifoo.scarl.core.{ActorQueue, State}
import io.github.fiifoo.scarl.geometry.WaypointNetwork
import io.github.fiifoo.scarl.world.{WorldManager, WorldState}

object GameUtils {

  def finalizeLoadedWorld(world: WorldState, manager: WorldManager): WorldState = {
    world.copy(
      areas = manager.areas,
      states = world.states.map(x => {
        var (area, state) = x

        state = state.copy(
          assets = Assets(
            combatPower = manager.combatPower
          ),
          communications = state.communications.copy(manager.communications),
          index = calculateStateIndex(state),
          kinds = manager.kinds,
          powers = manager.powers,
          progressions = manager.progressions
        )
        state = state.copy(
          cache = calculateStateCache(state)
        )

        (area, state)
      })
    )
  }

  private def calculateStateCache(s: State): State.Cache = {
    val cache = s.cache

    val equipmentStats = s.equipments.keys.foldLeft(cache.equipmentStats)((stats, creature) => {
      EquipmentStatsCacheMutation(creature)(s, stats)
    })

    cache.copy(
      actorQueue = ActorQueue(s),
      equipmentStats = equipmentStats,
      waypointNetwork = WaypointNetwork(s)
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

    index
  }
}
