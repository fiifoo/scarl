package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.ai.tactic.RoamTactic
import io.github.fiifoo.scarl.area.{AreaId, Conduit}
import io.github.fiifoo.scarl.core._
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.CombinedEffectListener
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.mutation.ResetConduitEntryMutation
import io.github.fiifoo.scarl.geometry.Fov
import io.github.fiifoo.scarl.message.MessageBuilder
import io.github.fiifoo.scarl.world.{WorldManager, WorldState}

import scala.annotation.tailrec

class Game(out: OutConnection,
           player: Player,
           worldManager: WorldManager,
           initialWorld: WorldState,
           initialArea: AreaId
          ) {

  private val messages = new MessageBuilder(player)
  private val statistics = new StatisticsBuilder()

  private var world = initialWorld
  private var area = initialArea
  private var (bubble, state) = createBubble(world.states(area))

  def receive(action: Action): Unit = {
    run(Some(action))
  }

  private def initialize(): Unit = {
    sendInitial(state)
    run(None)
  }

  private def run(action: Option[Action]): Unit = {
    state = process(bubble, state, action)

    if (hasConduitEntry(state)) {
      val (nextWorld, nextArea, nextBubble, nextState, playerCreature) = handleConduitEntry(world, area, bubble, state)
      player.creature = playerCreature
      world = nextWorld
      area = nextArea
      bubble = nextBubble
      state = process(bubble, nextState, None)
    }

    if (gameOver(state)) {
      sendFinal(state)
    } else {
      send(state)
    }
  }

  private def handleConduitEntry(w: WorldState,
                                 a: AreaId,
                                 b: RealityBubble,
                                 s: State
                                ): (WorldState, AreaId, RealityBubble, State, CreatureId) = {

    val (conduit, creature) = s.tmp.conduitEntry.get
    var currentState = ResetConduitEntryMutation()(s)

    if (creature.id == player.creature) {
      currentState = b.save(currentState)
      val (nextWorld, playerCreature) = worldManager.switchArea(w, a, currentState, conduit, creature)
      val nextArea = getNextArea(w.conduits(conduit), a)
      val (nextBubble, nextState) = createBubble(nextWorld.states(nextArea))

      (nextWorld, nextArea, nextBubble, nextState, playerCreature)
    } else {
      // puf, gone
      (w, a, b, currentState, player.creature)
    }
  }

  @tailrec
  private def process(b: RealityBubble, s: State, action: Option[Action]): State = {
    if (gameOver(s) || hasConduitEntry(s)) {
      return s
    }

    if (b.nextActor.contains(player.creature)) {
      if (action.isDefined) {
        process(b, b(s, action), None)
      } else {
        s
      }
    } else {
      process(b, b(s, None), action)
    }
  }

  private def sendInitial(s: State): Unit = {
    updateFov(s)
    out(s, area, messages.extract(), kinds = Some(s.kinds))
  }

  private def send(s: State): Unit = {
    updateFov(s)
    out(s, area, messages.extract())
  }

  private def sendFinal(s: State): Unit = {
    out(s, area, messages.extract(), statistics = Some(statistics.get()))
  }

  private def updateFov(s: State): Unit = {
    player.fov = Fov(s)(player.creature(s).location, 10)
  }

  private def gameOver(s: State): Boolean = {
    !s.entities.isDefinedAt(player.creature)
  }

  private def hasConduitEntry(s: State): Boolean = {
    s.tmp.conduitEntry.nonEmpty
  }

  private def getNextArea(conduit: Conduit, currentArea: AreaId): AreaId = {
    if (conduit.source == currentArea) {
      conduit.target
    } else {
      conduit.source
    }
  }

  private def createBubble(s: State): (RealityBubble, State) = {
    RealityBubble(
      initial = s,
      ai = RoamTactic,
      listener = new Listener(effect = new CombinedEffectListener(List(
        messages,
        statistics
      )))
    )
  }

  initialize()
}
