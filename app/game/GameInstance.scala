package game

import akka.actor.ActorRef
import dal.GameRepository
import game.json.FormatGameState._
import game.json.{ReadInMessage, WriteOutMessage}
import io.github.fiifoo.scarl.game.api.OutMessage
import io.github.fiifoo.scarl.game.{Game => Engine}
import models.Game
import models.message.{CreateExistingGame, CreateGameMessage, CreateNewGame}
import play.api.libs.json._

import scala.concurrent.{ExecutionContext, Future}

object GameInstance {

  def apply(games: GameRepository, message: CreateGameMessage, out: ActorRef)
           (implicit ec: ExecutionContext): Future[Option[GameInstance]] = {

    val game = message match {
      case message: CreateNewGame => games.create(message.player) map Some.apply
      case message: CreateExistingGame => games.start(message.game)
    }

    game map (_ map (game => new GameInstance(games, game, out)))
  }
}

class GameInstance(games: GameRepository, game: Game, out: ActorRef)
                  (implicit ec: ExecutionContext) {

  var state = StartGame(game.save map Json.parse)
  sendMessages()

  def receive(json: JsValue): Unit = {
    state = Engine.receive(state, ReadInMessage(json))
    sendMessages()
  }

  def stop(): Unit = {
    if (state.ended) {
      end()
    } else {
      save()
    }
  }

  private def save(): Unit = {
    val gameState = Engine.save(state)
    val json = SaveGame(gameState)

    games.save(game.id, json.toString())
  }

  private def end(): Unit = {
    games.delete(game.id)
  }

  private def sendMessages(): Unit = {
    state.outMessages.reverse.foreach(send)
    state = state.copy(outMessages = Nil)
  }

  private def send(data: OutMessage): Unit = {
    out ! WriteOutMessage(data)
  }
}
