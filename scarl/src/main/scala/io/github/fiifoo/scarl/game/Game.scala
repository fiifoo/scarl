package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.ai.tactic.RoamTactic
import io.github.fiifoo.scarl.core._
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.CombinedEffectListener
import io.github.fiifoo.scarl.core.entity.Creature
import io.github.fiifoo.scarl.core.mutation.ResetConduitEntryMutation
import io.github.fiifoo.scarl.geometry.Fov
import io.github.fiifoo.scarl.message.MessageBuilder
import io.github.fiifoo.scarl.world.WorldManager

import scala.annotation.tailrec

class Game(initial: GameState,
           out: OutConnection,
           worldManager: WorldManager
          ) {

  private var gameState = initial
  private var fov: Set[Location] = Set()

  private val messageBuilder = new MessageBuilder(() => gameState.player, () => fov)
  private val statisticsBuilder = new StatisticsBuilder()

  private var (bubble, state) = createBubble(gameState.world.states(gameState.area))

  def receive(action: Action): Unit = {
    run(Some(action))
  }

  def save(): GameState = {
    val world = gameState.world
    val area = gameState.area

    gameState.copy(world = world.copy(
      states = world.states + (area -> bubble.save(state))
    ))
  }

  def over: Boolean = {
    gameOver(state)
  }

  private def initialize(): Unit = {
    updateFov()
    sendInitial()
    run(None)
  }

  private def run(action: Option[Action]): Unit = {
    state = process(state, action)

    getConduitEntry(state).foreach(handleConduitEntry)

    if (gameOver(state)) {
      sendFinal()
    } else {
      updateFov()
      send()
    }
  }

  private def handleConduitEntry(entry: (ConduitId, Creature)): Unit = {
    val (conduit, creature) = entry
    state = ResetConduitEntryMutation()(state)

    if (creature.id == gameState.player) {
      switchArea(conduit, creature)
    }

    state = process(state, None)
  }

  private def switchArea(conduit: ConduitId, player: Creature): Unit = {
    val (nextWorld, nextArea, nextPlayer) = worldManager.switchArea(
      gameState.world,
      gameState.area,
      bubble.save(state),
      conduit,
      player
    )
    val (nextBubble, nextState) = createBubble(nextWorld.states(nextArea))

    gameState = gameState.copy(nextArea, nextPlayer, nextWorld)
    bubble = nextBubble
    state = nextState
  }

  @tailrec
  private def process(s: State, action: Option[Action]): State = {
    if (gameOver(s) || getConduitEntry(s).isDefined) {
      return s
    }

    if (bubble.nextActor.contains(gameState.player)) {
      if (action.isDefined) {
        process(bubble(s, action), None)
      } else {
        s
      }
    } else {
      process(bubble(s, None), action)
    }
  }

  private def sendInitial(): Unit = {
    out(gameState, state, fov, messageBuilder.extract(), kinds = Some(state.kinds))
  }

  private def send(): Unit = {
    out(gameState, state, fov, messageBuilder.extract())
  }

  private def sendFinal(): Unit = {
    out(gameState, state, fov, messageBuilder.extract(), statistics = Some(statisticsBuilder.get()))
  }

  private def updateFov(): Unit = {
    fov = Fov(state)(gameState.player(state).location, 10)
  }

  private def gameOver(s: State): Boolean = {
    !s.entities.isDefinedAt(gameState.player)
  }

  private def getConduitEntry(s: State): Option[(ConduitId, Creature)] = {
    s.tmp.conduitEntry
  }

  private def createBubble(s: State): (RealityBubble, State) = {
    RealityBubble(
      initial = s,
      ai = RoamTactic,
      listener = new Listener(effect = new CombinedEffectListener(List(
        messageBuilder,
        statisticsBuilder
      )))
    )
  }

  initialize()
}
