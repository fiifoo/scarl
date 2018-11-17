package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.core.RealityBubble
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.mutation.{FinalizeTickMutation, ResetConduitEntryMutation}
import io.github.fiifoo.scarl.core.world.{ConduitId, Traveler}
import io.github.fiifoo.scarl.game.api._
import io.github.fiifoo.scarl.game.area.MapBuilder
import io.github.fiifoo.scarl.game.event.EventBuilder
import io.github.fiifoo.scarl.game.player.{PlayerFov, PlayerInfo}
import io.github.fiifoo.scarl.game.statistics.StatisticsBuilder
import io.github.fiifoo.scarl.world.ChangeArea

import scala.annotation.tailrec

object RunGame {

  def apply(state: RunState, action: Option[Action] = None): RunState = {
    if (state.ended) {
      state
    } else {
      run(state.copy(stopped = false, paused = false), action)
    }
  }

  @tailrec
  private def run(state: RunState, action: Option[Action] = None): RunState = {
    if (state.ended) {
      return sendGameOver(sendGameUpdate(state))
    } else if (state.stopped) {
      return sendGameUpdate(state)
    } else if (state.paused) {
      return state
    }

    var (nextState, nextAction) = if (playerTurn(state)) {
      val nextState = if (action.isDefined) {
        tick(state, action).copy(paused = true)
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
    RealityBubble(state.instance, action) map (result => {
      val instance = result.state
      val effects = result.effects

      val events = state.events ::: EventBuilder(
        instance,
        state.game.player,
        state.fov.locations,
        effects
      )

      val statistics = StatisticsBuilder(instance, state.statistics, effects)
      val ended = state.game.player(instance).dead

      state.copy(
        ended = ended,
        events = events,
        fov = if (ended) state.fov.next(instance, state.game.player) else state.fov,
        instance = FinalizeTickMutation()(instance),
        playerInfo = PlayerInfo(instance, state.game.player),
        statistics = statistics
      )
    }) getOrElse state
  }

  private def playerTurn(state: RunState): Boolean = {
    state.instance.cache.actorQueue.headOption.contains(state.game.player)
  }

  private def getConduitEntry(state: RunState): Option[(ConduitId, Traveler)] = {
    state.instance.tmp.conduitEntry
  }

  private def handleConduitEntry(state: RunState)(entry: (ConduitId, Traveler)): RunState = {
    val (conduit, traveler) = entry

    val nextState = state.copy(
      instance = ResetConduitEntryMutation()(state.instance)
    )

    if (traveler.creature.id == state.game.player) {
      changeArea(nextState, conduit, traveler)
    } else {
      nextState
    }
  }

  private def changeArea(state: RunState, conduit: ConduitId, traveler: Traveler): RunState = {
    val (nextWorld, nextArea) = ChangeArea(
      state.game.world,
      state.game.area,
      state.instance,
      conduit,
      traveler
    )
    val nextMaps = state.game.maps + (state.game.area -> state.areaMap)
    val nextGameState = state.game.copy(
      area = nextArea,
      maps = nextMaps,
      world = nextWorld
    )
    val nextInstance = nextWorld.states(nextArea)

    val nextState = state.copy(
      areaMap = state.game.maps.getOrElse(nextArea, Map()),
      fov = PlayerFov(),
      game = nextGameState,
      instance = nextInstance,
      playerInfo = PlayerInfo(nextInstance, nextGameState.player)
    )

    sendAreaChange(nextState)
  }

  private def sendGameUpdate(state: RunState): RunState = {
    val nextState = updateFov(state)
    val message = GameUpdate(nextState)

    sendMessage(nextState.copy(events = Nil), message)
  }

  private def sendGameOver(state: RunState): RunState = {
    val message = GameOver(state)

    sendMessage(state, message)
  }

  private def sendAreaChange(state: RunState): RunState = {
    val message = AreaChange(state)

    sendMessage(state, message)
  }

  private def sendMessage(state: RunState, message: OutMessage): RunState = {
    state.copy(
      outMessages = message :: state.outMessages
    )
  }

  private def updateFov(state: RunState): RunState = {
    val fov = if (state.ended) state.fov else state.fov.next(state.instance, state.game.player)
    val areaMap = state.areaMap ++ MapBuilder(fov)

    state.copy(
      fov = fov,
      areaMap = areaMap
    )
  }
}
