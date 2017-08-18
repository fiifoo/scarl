package controllers

import javax.inject.Inject

import akka.actor._
import akka.stream.Materializer
import game.GameContainer
import game.save.{FileSaveStorage, NullSaveStorage}
import play.Environment
import play.api.libs.json.JsValue
import play.api.libs.streams._
import play.api.mvc._

import scala.concurrent.ExecutionContext

class GameController @Inject()(cc: ControllerComponents
                              )(
                                implicit val ec: ExecutionContext,
                                implicit val system: ActorSystem,
                                mat: Materializer,
                                environment: Environment
                              ) extends AbstractController(cc) {

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
    val game = GameContainer(out, saveStorage)

    def receive = {
      case json: JsValue => game.receive(json)
    }

    override def postStop() = {
      game.end()
    }
  }

}
