package controllers

import javax.inject.Inject

import akka.actor._
import akka.stream.Materializer
import io.github.fiifoo.scarl.game.OutMessage
import models.GameManager
import models.json.{ReadAction, WriteOutMessage}
import models.save.{FileSaveStorage, NullSaveStorage}
import play.Environment
import play.api.libs.json.JsValue
import play.api.libs.streams._
import play.api.mvc._

class GameController @Inject()(implicit system: ActorSystem, materializer: Materializer, environment: Environment) extends Controller {

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
    val game = manager.loadOrCreate(send)

    def send = (data: OutMessage) => {
      out ! WriteOutMessage(data)
    }

    def receive = {
      case json: JsValue => game.receive(ReadAction(json))
    }

    override def postStop() = {
      if (game.over) {
        manager.end(game)
      } else {
        manager.save(game)
      }
    }
  }

}
