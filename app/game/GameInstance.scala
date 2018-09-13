package game

import akka.actor.ActorRef
import dal.GameRepository
import io.github.fiifoo.scarl.game.api._
import io.github.fiifoo.scarl.game.{CalculateBrains, RunState}
import io.github.fiifoo.scarl.world.WorldAssets
import models.Game
import models.json.{ReadInMessage, WriteOutMessage}
import models.message.{CreateExistingGame, CreateGameMessage, CreateNewGame}
import play.api.libs.json._

import scala.annotation.tailrec
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

  var state = CreateGame(this.assets, this.game.save map Json.parse)
  state = this.sendMessages(state)

  def receive(json: JsValue): Unit = {
    if (state.ended) {
      return
    }

    val message = ReadInMessage(json)

    this.applyMessage(message)
  }

  def stop(): Unit = {
    if (state.ended) {
      this.end()
    } else {
      this.save()
    }
  }

  @tailrec
  private def applyMessage(message: InMessage): Unit = {
    val (nextState, nextMessage) = message.apply(state)

    state = nextState
    state = this.sendMessages(state)

    state.brains foreach (brains => {
      state = CalculateBrains.commit(state, brains)
      state = state.copy(brains = None)
    })

    if (nextMessage.isDefined) {
      applyMessage(nextMessage.get)
    }
  }

  private def save(): Unit = {
    val json = SaveGame(state)
    this.games.save(this.game.id, json.toString())
  }

  private def end(): Unit = {
    this.games.delete(this.game.id)
  }

  private def sendMessages(state: RunState): RunState = {
    state.outMessages.reverse.foreach(this.send)

    state.copy(outMessages = Nil)
  }

  private def send(data: OutMessage): Unit = {
    this.out ! WriteOutMessage(data)
  }
}
