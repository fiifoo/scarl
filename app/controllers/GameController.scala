package controllers

import javax.inject.Inject

import akka.actor._
import akka.stream.Materializer
import play.Environment
import play.api.libs.json.JsValue
import play.api.libs.streams._
import play.api.mvc._

class GameController @Inject()(implicit system: ActorSystem, materializer: Materializer, environment: Environment) extends Controller {

  class MyWebSocketActor(out: ActorRef) extends Actor {
    def receive = {
      case msg: JsValue =>
        out ! msg
    }
  }

  object MyWebSocketActor {
    def props(out: ActorRef) = Props(new MyWebSocketActor(out))
  }

  def socket = WebSocket.accept[JsValue, JsValue] { request =>
    ActorFlow.actorRef(out => MyWebSocketActor.props(out))
  }

  def index = Action {
    val assets = if (environment.isDev) {
      "http://localhost:80"
    } else {
      routes.Assets.versioned("").toString
    }

    Ok(views.html.index(assets))
  }

}
