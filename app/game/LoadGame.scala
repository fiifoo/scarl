package game

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.ActorQueue
import io.github.fiifoo.scarl.core.geometry.WaypointNetwork
import io.github.fiifoo.scarl.core.mutation.cache.EquipmentStatsCacheMutation
import io.github.fiifoo.scarl.core.mutation.index.{ConduitLocationIndexAddMutation, ItemFinderIndexAddMutation, NewEntityIndexMutation}
import io.github.fiifoo.scarl.game.GameState
import io.github.fiifoo.scarl.world.{WorldAssets, WorldState}
import models.GameSave
import play.api.libs.json.JsValue

object LoadGame {

  def apply(assets: WorldAssets)(json: JsValue): GameState = {
    val data = GameSave.format.reads(json).get
    val world = finalize(assets, data.state.world)

    val gameState = GameState(
      area = data.state.area,
      player = data.state.player,
      world = world,
      maps = data.state.maps,
      statistics = data.state.statistics
    )

    if (data.checkHashCode != gameState.hashCode) {
      throw new Exception("Corrupt save data.")
    }

    gameState
  }

  def finalize(assets: WorldAssets, world: WorldState): WorldState = {
    world.copy(
      assets = assets,
      states = world.states.map(x => {
        var (area, state) = x

        state = state.copy(
          assets = assets.instance(),
          index = calculateStateIndex(state),
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

    val equipmentStats = (s.equipments.keys foldLeft cache.equipmentStats) ((stats, creature) => {
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

    index = (s.entities.values foldLeft index) ((index, entity) => {
      NewEntityIndexMutation(entity)(s, index)
    })
    index = (s.conduits.entrances foldLeft index) ((index, x) => {
      val (conduit, location) = x

      index.copy(
        locationConduit = ConduitLocationIndexAddMutation(conduit, location)(index.locationConduit)
      )
    })
    index = (s.foundItems foldLeft index) ((index, x) => {
      val (finder, items) = x

      (items foldLeft index) ((index, item) => {
        index.copy(
          itemFinders = ItemFinderIndexAddMutation(finder, item)(index.itemFinders)
        )
      })
    })

    index
  }
}
