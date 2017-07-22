package controllers

import javax.inject.Inject

import akka.actor._
import akka.stream.Materializer
import io.github.fiifoo.scarl.game.api.OutMessage
import models.GameManager
import models.json.{ReadInMessage, WriteOutMessage}
import models.save.{FileSaveStorage, NullSaveStorage}
import play.Environment
import play.api.libs.json.JsValue
import play.api.libs.streams._
import play.api.mvc._

class GameController @Inject()(cc: ControllerComponents)(implicit system: ActorSystem, mat: Materializer, environment: Environment) extends AbstractController(cc) {

  val assets = if (environment.isDev) {
    "http://localhost:80"
  } else {
    routes.Assets.versioned("").toString
  }

  val saveStorage = if (environment.isDev) {
    new FileSaveStorage("user")
  } else {
    NullSaveStorage
  }

  def index = Action {
    Ok(views.html.index(assets))
  }

  def socket = WebSocket.accept[JsValue, JsValue] { _ =>
    ActorFlow.actorRef(out => WebSocketActor.props(out))
  }

  object WebSocketActor {
    def props(out: ActorRef) = Props(new WebSocketActor(out))
  }

  class WebSocketActor(out: ActorRef) extends Actor {

    val manager = new GameManager(saveStorage)
    var (game, state) = manager.loadOrCreate()
    sendMessages()

    def sendMessages() = {
      state.outMessages.reverse.foreach(send)
      state = state.copy(outMessages = Nil)
    }

    def send(data: OutMessage) = {
      out ! WriteOutMessage(data)
    }

    def receive = {
      case json: JsValue =>
        state = game.receive(state, ReadInMessage(json))
        sendMessages()
    }

    override def postStop() = {
      if (state.ended) {
        manager.end()
      } else {
        manager.save(game, state)
      }
    }
  }

}
