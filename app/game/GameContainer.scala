package game

import akka.actor.ActorRef
import game.json.FormatGameState._
import game.json.{ReadInMessage, WriteOutMessage}
import game.save.SaveStorage
import io.github.fiifoo.scarl.game._
import io.github.fiifoo.scarl.game.api.OutMessage
import play.api.libs.json._

object GameContainer {
  def apply(out: ActorRef, saveStorage: SaveStorage): GameContainer = {
    new GameContainer(out, saveStorage)
  }
}

class GameContainer(out: ActorRef, saveStorage: SaveStorage) {
  var state = StartGame(saveStorage.load())
  sendMessages()

  def receive(json: JsValue): Unit = {
    state = Game.receive(state, ReadInMessage(json))
    sendMessages()
  }

  def end(): Unit = {
    if (state.ended) {
      saveStorage.clear()
    } else {
      save()
    }
  }

  private def save(): Unit = {
    val gameState = Game.save(state)
    val json = SaveGame(gameState)

    saveStorage.save(json)
  }

  private def sendMessages(): Unit = {
    state.outMessages.reverse.foreach(send)
    state = state.copy(outMessages = Nil)
  }

  private def send(data: OutMessage): Unit = {
    out ! WriteOutMessage(data)
  }
}
