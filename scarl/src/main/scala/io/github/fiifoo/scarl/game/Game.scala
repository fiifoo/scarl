package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.action.validate.ActionValidator
import io.github.fiifoo.scarl.ai.tactic.RoamTactic
import io.github.fiifoo.scarl.core.RealityBubble
import io.github.fiifoo.scarl.core.Selectors.{getContainerItems, getEquipmentStats}
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.mutation.{RemoveEntitiesMutation, ResetConduitEntryMutation}
import io.github.fiifoo.scarl.core.world.{ConduitId, Traveler}
import io.github.fiifoo.scarl.effect.DeathEffect
import io.github.fiifoo.scarl.game.api.OutMessage.PlayerInfo
import io.github.fiifoo.scarl.game.api._
import io.github.fiifoo.scarl.game.event.EventBuilder
import io.github.fiifoo.scarl.game.map.MapBuilder
import io.github.fiifoo.scarl.geometry.Fov
import io.github.fiifoo.scarl.world.WorldManager

import scala.annotation.tailrec

object Game {

  def apply(gameState: GameState, worldManager: WorldManager): (Game, RunState) = {

    val instance = gameState.world.states(gameState.area)
    val bubble = RealityBubble(RoamTactic)
    val game = new Game(bubble, worldManager)

    var state = RunState(
      areaMap = gameState.maps.getOrElse(gameState.area, Map()),
      gameState = gameState,
      instance = instance,
      statistics = gameState.statistics
    )

    state = game.sendGameStart(state)
    state = game.run(state)

    (game, state)
  }

}

class Game(bubble: RealityBubble, worldManager: WorldManager) {

  def receive(state: RunState, message: InMessage): RunState = {
    message match {
      case DebugFovQuery => sendMessage(state, DebugFov(state.fov.locations))
      case DebugWaypointQuery => sendMessage(state, DebugWaypoint(state.instance.cache.waypointNetwork))
      case message: GameAction => receiveAction(state, message.action)
      case InventoryQuery => sendPlayerInventory(state)
    }
  }

  def save(state: RunState): GameState = {
    val maps = state.gameState.maps
    val world = state.gameState.world
    val area = state.gameState.area
    val instance = RemoveEntitiesMutation()(state.instance)

    state.gameState.copy(
      maps = maps + (area -> state.areaMap),
      statistics = state.statistics,
      world = world.copy(
        states = world.states + (area -> instance)
      ))
  }

  private def receiveAction(state: RunState, action: Action): RunState = {
    if (!ActionValidator(state.instance, state.gameState.player, action)) {
      return state
    }

    run(state.copy(stopped = false), Some(action))
  }

  @tailrec
  private def run(state: RunState, action: Option[Action] = None): RunState = {
    if (state.ended) {
      return sendGameOver(state)
    } else if (state.stopped) {
      return sendGameUpdate(state)
    }

    var (nextState, nextAction) = if (playerTurn(state)) {
      val nextState = if (action.isDefined) {
        tick(state, action)
      } else {
        state.copy(stopped = true)
      }

      (nextState, None)
    } else {
      val nextState = tick(state)

      (nextState, action)
    }

    nextState = getConduitEntry(nextState) map handleConduitEntry(nextState) getOrElse nextState

    run(nextState, nextAction)
  }

  private def tick(state: RunState, action: Option[Action] = None): RunState = {
    bubble(state.instance, action) map (result => {
      val instance = result.state
      val effects = result.effects

      val events = state.events ::: EventBuilder(
        instance,
        state.gameState.player,
        state.fov.locations,
        effects
      )
      val statistics = StatisticsBuilder(instance, state.statistics, effects)
      val ended = (effects collectFirst {
        case effect: DeathEffect if effect.target == state.gameState.player => true
      }) getOrElse false

      state.copy(
        ended = ended,
        events = events,
        instance = instance,
        statistics = statistics
      )
    }) getOrElse state
  }

  private def playerTurn(state: RunState): Boolean = {
    state.instance.cache.actorQueue.headOption.contains(state.gameState.player)
  }

  private def getConduitEntry(state: RunState): Option[(ConduitId, Traveler)] = {
    state.instance.tmp.conduitEntry
  }

  private def handleConduitEntry(state: RunState)(entry: (ConduitId, Traveler)): RunState = {
    val (conduit, traveler) = entry

    val nextState = state.copy(
      instance = ResetConduitEntryMutation()(state.instance)
    )

    if (traveler.creature.id == state.gameState.player) {
      switchArea(nextState, conduit, traveler)
    } else {
      nextState
    }
  }

  private def switchArea(state: RunState, conduit: ConduitId, traveler: Traveler): RunState = {
    val instance = RemoveEntitiesMutation()(state.instance)

    val (nextWorld, nextArea) = worldManager.switchArea(
      state.gameState.world,
      state.gameState.area,
      instance,
      conduit,
      traveler
    )
    val nextMaps = state.gameState.maps + (state.gameState.area -> state.areaMap)
    val nextGameState = state.gameState.copy(
      area = nextArea,
      maps = nextMaps,
      world = nextWorld
    )
    val nextInstance = nextWorld.states(nextArea)

    val nextState = state.copy(
      areaMap = state.gameState.maps.getOrElse(nextArea, Map()),
      fov = PlayerFov(),
      gameState = nextGameState,
      instance = nextInstance
    )

    sendAreaChange(nextState)
  }

  private def sendGameStart(state: RunState): RunState = {
    val message = GameStart(
      area = state.gameState.area,
      factions = state.instance.factions.values,
      kinds = state.instance.kinds,
      map = state.areaMap
    )

    sendMessage(state, message)
  }

  private def sendGameUpdate(state: RunState): RunState = {
    val events = state.events
    val nextState = updateFov(state)
      .copy(events = Nil)

    val message = GameUpdate(
      fov = nextState.fov,
      events = events,
      player = PlayerInfo(
        creature = state.gameState.player(state.instance),
        equipmentStats = getEquipmentStats(state.instance)(state.gameState.player)
      )
    )

    sendMessage(nextState, message)
  }

  private def sendGameOver(state: RunState): RunState = {
    val message = GameOver(
      statistics = state.statistics
    )

    sendMessage(state, message)
  }

  private def sendAreaChange(state: RunState): RunState = {
    val message = AreaChange(
      area = state.gameState.area,
      map = state.areaMap
    )

    sendMessage(state, message)
  }

  private def sendPlayerInventory(state: RunState): RunState = {
    val message = PlayerInventory(
      inventory = getContainerItems(state.instance)(state.gameState.player) map (_ (state.instance)),
      equipments = state.instance.equipments.getOrElse(state.gameState.player, Map())
    )

    sendMessage(state, message)
  }

  private def sendMessage(state: RunState, message: OutMessage): RunState = {
    state.copy(
      outMessages = (message :: state.outMessages).reverse
    )
  }

  private def updateFov(state: RunState): RunState = {
    val creature = state.gameState.player(state.instance)
    val locations = Fov(state.instance)(creature.location, creature.stats.sight.range)

    val areaMap = state.areaMap ++ MapBuilder(state.fov)
    val fov = state.fov.next(state.instance, locations)

    state.copy(
      fov = fov,
      areaMap = areaMap
    )
  }
}
