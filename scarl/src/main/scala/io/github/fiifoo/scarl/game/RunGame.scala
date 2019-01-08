package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.core.RealityBubble
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.mutation.{FinalizeTickMutation, ResetConduitEntryMutation}
import io.github.fiifoo.scarl.core.world.ConduitId
import io.github.fiifoo.scarl.game.api._
import io.github.fiifoo.scarl.game.area.MapBuilder
import io.github.fiifoo.scarl.game.event.EventBuilder
import io.github.fiifoo.scarl.game.player.PlayerInfo
import io.github.fiifoo.scarl.game.statistics.StatisticsBuilder

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

    nextState = getConduitEntry(nextState) map applyConduitEntry(nextState) getOrElse nextState

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

  private def getConduitEntry(state: RunState): Option[List[(CreatureId, ConduitId)]] = {
    state.instance.tmp.conduitEntry
  }

  private def applyConduitEntry(state: RunState)(entries: List[(CreatureId, ConduitId)]): RunState = {
    val initial = state.copy(
      instance = ResetConduitEntryMutation()(state.instance)
    )

    (entries foldLeft initial) ((state, entry) => {
      val (creature, conduit) = entry

      // npc travelers not supported for now
      if (state.instance.entities.isDefinedAt(creature) && creature == state.game.player) {
        sendAreaChange(changeArea(state, conduit))
      } else {
        state
      }
    })
  }

  private def changeArea(state: RunState, conduitId: ConduitId): RunState = {
    val conduit = state.game.world.conduits(conduitId)

    val area = if (conduit.source == state.game.area) {
      conduit.target
    } else {
      conduit.source
    }

    ChangeArea(area, Some(conduit.id))(state)
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
