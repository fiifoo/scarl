package controllers

import javax.inject.Inject

import akka.actor._
import akka.stream.Materializer
import models.{Game, Player}
import play.Environment
import play.api.libs.json.JsValue
import play.api.libs.streams._
import play.api.mvc._

class GameController @Inject()(implicit system: ActorSystem, materializer: Materializer, environment: Environment) extends Controller {

  val game = new Game()

  def index = Action {
    val assets = if (environment.isDev) {
      "http://localhost:80"
    } else {
      routes.Assets.versioned("").toString
    }

    Ok(views.html.index(assets))
  }

  def socket = WebSocket.accept[JsValue, JsValue] { request =>
    ActorFlow.actorRef(out => WebSocketActor.props(out))
  }

  object WebSocketActor {
    def props(out: ActorRef) = Props(new WebSocketActor(out))
  }

  class WebSocketActor(out: ActorRef) extends Actor {

    import io.github.fiifoo.scarl.action.PassAction

    val player = new Player(sendEntities)
    val actionReceiver = game.receivePlayer(player)

    def sendEntities = (entities: JsValue) => {
      out ! entities
    }

    def receive = {
      case _: JsValue => actionReceiver(PassAction())
    }
  }

}
