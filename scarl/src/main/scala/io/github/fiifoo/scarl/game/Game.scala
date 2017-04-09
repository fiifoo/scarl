package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.action.validate.ValidateAction
import io.github.fiifoo.scarl.ai.tactic.RoamTactic
import io.github.fiifoo.scarl.core._
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.CombinedEffectListener
import io.github.fiifoo.scarl.core.entity.Creature
import io.github.fiifoo.scarl.core.kind.Kinds
import io.github.fiifoo.scarl.core.mutation.ResetConduitEntryMutation
import io.github.fiifoo.scarl.game.map.{MapBuilder, MapLocation}
import io.github.fiifoo.scarl.geometry.Fov
import io.github.fiifoo.scarl.message.MessageFactory
import io.github.fiifoo.scarl.world.WorldManager

import scala.annotation.tailrec

class Game(initial: GameState,
           worldManager: WorldManager,
           out: OutMessage => Unit
          ) {

  private var gameState = initial
  private var fov = PlayerFov()

  private val messageFactory = new MessageFactory(() => gameState.player, () => fov.locations)
  private val statisticsBuilder = new StatisticsBuilder(gameState.statistics)
  private val mapBuilder = new MapBuilder(areaMap)

  private var (bubble, state) = createBubble(gameState.world.states(gameState.area))

  def receive(action: Action): Unit = {
    if (shouldRun(action)) {
      run(Some(action))
    }
  }

  def save(): GameState = {
    val maps = gameState.maps
    val world = gameState.world
    val area = gameState.area

    gameState.copy(
      maps = maps + (area -> mapBuilder.extract()),
      statistics = statisticsBuilder.get(),
      world = world.copy(
        states = world.states + (area -> bubble.save(state))
      ))
  }

  def over: Boolean = {
    gameOver(state)
  }

  private def initialize(): Unit = {
    sendInitial()
    run(None)
  }

  private def run(action: Option[Action]): Unit = {
    state = process(state, action)

    conduitEntry(state).foreach(handleConduitEntry)

    if (gameOver(state)) {
      sendFinal()
    } else {
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
    val nextMaps = gameState.maps + (gameState.area -> mapBuilder.extract(gameState.maps.get(nextArea)))

    gameState = gameState.copy(area = nextArea, maps = nextMaps, player = nextPlayer, world = nextWorld)
    bubble = nextBubble
    state = nextState
    fov = PlayerFov()

    sendArea()
  }

  @tailrec
  private def process(s: State, action: Option[Action]): State = {
    if (gameOver(s) || conduitEntry(s).isDefined) {
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
    updateFov()
    out(outMessage(map = areaMap, kinds = Some(state.kinds)))
  }

  private def send(): Unit = {
    updateFov()
    out(outMessage())
  }

  private def sendArea(): Unit = {
    updateFov()
    out(outMessage(map = areaMap))
  }

  private def sendFinal(): Unit = {
    out(outMessage(statistics = Some(statisticsBuilder.get())))
  }

  private def updateFov(): Unit = {
    val creature = gameState.player(state)
    val locations = Fov(state)(creature.location, creature.stats.sight.range)

    fov = fov.next(state, locations)
    mapBuilder(fov)
  }

  private def outMessage(kinds: Option[Kinds] = None,
                         map: Option[Map[Location, MapLocation]] = None,
                         statistics: Option[Statistics] = None
                        ): OutMessage = {
    val area = gameState.area
    val messages = messageFactory.extract()
    val player = state.entities.get(gameState.player) map (_.asInstanceOf[Creature])

    OutMessage(area, fov, messages, player, kinds, map, statistics)
  }

  private def areaMap: Option[Map[Location, MapLocation]] = {
    gameState.maps.get(gameState.area)
  }

  private def shouldRun(action: Action): Boolean = {
    !gameOver(state) && ValidateAction(state, gameState.player, action)
  }

  private def gameOver(s: State): Boolean = {
    !s.entities.isDefinedAt(gameState.player)
  }

  private def conduitEntry(s: State): Option[(ConduitId, Creature)] = {
    s.tmp.conduitEntry
  }

  private def createBubble(s: State): (RealityBubble, State) = {
    RealityBubble(
      initial = s,
      ai = RoamTactic,
      listener = new Listener(effect = new CombinedEffectListener(List(
        messageFactory,
        statisticsBuilder
      )))
    )
  }

  initialize()
}
