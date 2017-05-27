package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.action.validate.ActionValidator
import io.github.fiifoo.scarl.ai.tactic.RoamTactic
import io.github.fiifoo.scarl.core.Selectors.{getContainerItems, getEquipmentStats}
import io.github.fiifoo.scarl.core._
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.effect.CombinedEffectListener
import io.github.fiifoo.scarl.core.mutation.ResetConduitEntryMutation
import io.github.fiifoo.scarl.core.world.{ConduitId, Traveler}
import io.github.fiifoo.scarl.game.api.OutMessage.PlayerInfo
import io.github.fiifoo.scarl.game.api._
import io.github.fiifoo.scarl.game.event.EventBuilder
import io.github.fiifoo.scarl.game.map.{MapBuilder, MapLocation}
import io.github.fiifoo.scarl.geometry.Fov
import io.github.fiifoo.scarl.world.WorldManager

import scala.annotation.tailrec

class Game(initial: GameState,
           worldManager: WorldManager,
           out: OutMessage => Unit
          ) {

  private var gameState = initial
  private var fov = PlayerFov()

  private val eventBuilder = new EventBuilder(() => gameState.player, () => fov.locations)
  private val statisticsBuilder = new StatisticsBuilder(gameState.statistics)
  private val mapBuilder = new MapBuilder(areaMap)

  private var (bubble, state) = createBubble(gameState.world.states(gameState.area))

  def receive(message: InMessage): Unit = {
    message match {
      case message: GameAction => receiveAction(message.action)
      case _: InventoryQuery => sendPlayerInventory()
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

  private def receiveAction(action: Action): Unit = {
    if (shouldRun(action)) {
      run(Some(action))
    }
  }

  private def initialize(): Unit = {
    sendGameStart()
    run(None)
  }

  private def run(action: Option[Action]): Unit = {
    state = process(state, action)

    conduitEntry(state).foreach(handleConduitEntry)

    if (gameOver(state)) {
      sendGameOver()
    } else {
      sendGameUpdate()
    }
  }

  private def handleConduitEntry(entry: (ConduitId, Traveler)): Unit = {
    val (conduit, traveler) = entry
    state = ResetConduitEntryMutation()(state)

    if (traveler.creature.id == gameState.player) {
      switchArea(conduit, traveler)
    }

    state = process(state, None)
  }

  private def switchArea(conduit: ConduitId, traveler: Traveler): Unit = {
    val (nextWorld, nextArea) = worldManager.switchArea(
      gameState.world,
      gameState.area,
      bubble.save(state),
      conduit,
      traveler
    )
    val (nextBubble, nextState) = createBubble(nextWorld.states(nextArea))
    val nextMaps = gameState.maps + (gameState.area -> mapBuilder.extract(gameState.maps.get(nextArea)))

    gameState = gameState.copy(area = nextArea, maps = nextMaps, world = nextWorld)
    bubble = nextBubble
    state = nextState
    fov = PlayerFov()

    sendAreaChange()
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

  private def sendGameStart(): Unit = {
    out(GameStart(
      area = gameState.area,
      factions = state.factions.values,
      kinds = state.kinds,
      map = areaMap
    ))
  }

  private def sendGameUpdate(): Unit = {
    updateFov()

    out(GameUpdate(
      fov = fov,
      events = eventBuilder.extract(),
      player = PlayerInfo(
        creature = gameState.player(state),
        equipmentStats = getEquipmentStats(state)(gameState.player)
      )
    ))
  }

  private def sendGameOver(): Unit = {
    out(GameOver(
      statistics = statisticsBuilder.get()
    ))
  }

  private def sendAreaChange(): Unit = {
    out(AreaChange(
      area = gameState.area,
      map = areaMap
    ))
  }

  private def sendPlayerInventory(): Unit = {
    out(PlayerInventory(
      inventory = getContainerItems(state)(gameState.player) map (_ (state)),
      equipments = state.equipments.getOrElse(gameState.player, Map())
    ))
  }

  private def updateFov(): Unit = {
    val creature = gameState.player(state)
    val locations = Fov(state)(creature.location, creature.stats.sight.range)

    fov = fov.next(state, locations)
    mapBuilder(fov)
  }

  private def areaMap: Map[Location, MapLocation] = {
    gameState.maps.getOrElse(gameState.area, Map())
  }

  private def shouldRun(action: Action): Boolean = {
    !gameOver(state) && ActionValidator(state, gameState.player, action)
  }

  private def gameOver(s: State): Boolean = {
    !s.entities.isDefinedAt(gameState.player)
  }

  private def conduitEntry(s: State): Option[(ConduitId, Traveler)] = {
    s.tmp.conduitEntry
  }

  private def createBubble(s: State): (RealityBubble, State) = {
    RealityBubble(
      initial = s,
      ai = RoamTactic,
      listener = new Listener(effect = new CombinedEffectListener(List(
        eventBuilder,
        statisticsBuilder
      )))
    )
  }

  initialize()
}
