package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.core.RealityBubble
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.mutation.{FinalizeTickMutation, ResetConduitEntryMutation}
import io.github.fiifoo.scarl.core.world.ConduitId
import io.github.fiifoo.scarl.game.api._
import io.github.fiifoo.scarl.game.area.MapBuilder
import io.github.fiifoo.scarl.game.event.EventBuilder
import io.github.fiifoo.scarl.game.player.PlayerFov
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
      return sendGameOver(sendGameUpdate(updateFov(state)))
    } else if (state.stopped) {
      return sendGameUpdate(updateFov(state))
    } else if (state.paused) {
      return updateFov(state)
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
        instance = if (ended) instance else FinalizeTickMutation()(instance),
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

  private def updateFov(state: RunState): RunState = {
    val fov = PlayerFov(state.instance, state.game.player, state.previous map (_.fov))
    val areaMap = state.areaMap ++ MapBuilder(fov)

    state.copy(
      fov = fov,
      areaMap = areaMap
    )
  }

  private def sendGameUpdate(state: RunState): RunState = {
    val message = GameUpdate(state)

    state.addMessage(message)
  }

  private def sendGameOver(state: RunState): RunState = {
    val message = GameOver(state)

    state.addMessage(message)
  }

  private def sendAreaChange(state: RunState): RunState = {
    val message = AreaChange(state)

    state.addMessage(message)
  }
}
