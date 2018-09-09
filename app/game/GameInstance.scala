package game

import akka.actor.ActorRef
import dal.GameRepository
import io.github.fiifoo.scarl.action.EquipWeaponsAction
import io.github.fiifoo.scarl.action.validate.ActionValidator
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.entity.Selectors.getContainerItems
import io.github.fiifoo.scarl.core.item.Equipment.ArmorSlot
import io.github.fiifoo.scarl.game.api._
import io.github.fiifoo.scarl.game.{CalculateBrains, RunGame, RunState}
import io.github.fiifoo.scarl.world.WorldAssets
import models.Game
import models.json.{ReadInMessage, WriteOutMessage}
import models.message.{CreateExistingGame, CreateGameMessage, CreateNewGame}
import play.api.libs.json._

import scala.concurrent.{ExecutionContext, Future}

object GameInstance {

  def apply(games: GameRepository, assets: WorldAssets, message: CreateGameMessage, out: ActorRef)
           (implicit ec: ExecutionContext): Future[Option[GameInstance]] = {

    val game = message match {
      case message: CreateNewGame => games.create(message.player) map Some.apply
      case message: CreateExistingGame => games.start(message.game)
    }

    game map (_ flatMap (game => {
      try {
        val instance = new GameInstance(games, assets, game, out)

        Some(instance)
      } catch {
        case _: Exception =>
          games.stop(game.id)

          None
      }
    }))
  }
}

class GameInstance(games: GameRepository, assets: WorldAssets, game: Game, out: ActorRef)
                  (implicit ec: ExecutionContext) {

  var state = CreateGame(assets, game.save map Json.parse)
  state = sendMessages(state)

  def receive(json: JsValue): Unit = {
    val message = ReadInMessage(json)

    receiveMessage(message)
  }

  def stop(): Unit = {
    if (state.ended) {
      end()
    } else {
      save()
    }
  }

  private def receiveMessage(message: InMessage): Unit = {
    message match {
      case DebugFovQuery =>
        send(DebugFov(state.fov.locations))

      case DebugWaypointQuery =>
        send(DebugWaypoint(state.instance.cache.waypointNetwork))

      case message: GameAction =>
        if (!state.ended && ActionValidator(state.instance, state.gameState.player, message.action)) {
          run(message.action)
        }

      case InventoryQuery =>
        send(PlayerInventory(
          inventory = getContainerItems(state.instance)(state.gameState.player) map (_ (state.instance)),
          equipments = state.instance.equipments.getOrElse(state.gameState.player, Map())
        ))

      case message: SetEquipmentSet =>
        if (message.set != state.gameState.settings.equipmentSet) {
          val currentWeapons = state.instance.equipments
            .getOrElse(state.gameState.player, Map())
            .filterKeys(!_.isInstanceOf[ArmorSlot])

          val newWeapons = state.gameState.settings.equipmentSets
            .getOrElse(message.set, Map())
            .filter(x => {
              val (_, item) = x

              state.instance.entities.isDefinedAt(item) && item(state.instance).container == state.gameState.player
            })

          state = state.copy(gameState = state.gameState.copy(
            settings = state.gameState.settings.changeEquipmentSet(message.set, currentWeapons)
          ))

          send(PlayerSettings(state.gameState.settings))
          receiveMessage(GameAction(EquipWeaponsAction(newWeapons)))
          receiveMessage(InventoryQuery)
        }

      case message: SetQuickItem =>
        state = state.copy(gameState = state.gameState.copy(
          settings = state.gameState.settings.setQuickItem(message.slot, message.item)
        ))

        send(PlayerSettings(state.gameState.settings))
    }
  }

  private def run(action: Action): Unit = {
    state = RunGame(state, Some(action))

    val brains = CalculateBrains(state)

    state = RunGame(state)
    state = sendMessages(state)

    state = CalculateBrains.commit(state, brains)
  }

  private def save(): Unit = {
    val json = SaveGame(state)
    games.save(game.id, json.toString())
  }

  private def end(): Unit = {
    games.delete(game.id)
  }

  private def sendMessages(state: RunState): RunState = {
    state.outMessages.reverse.foreach(send)

    state.copy(outMessages = Nil)
  }

  private def send(data: OutMessage): Unit = {
    out ! WriteOutMessage(data)
  }
}
