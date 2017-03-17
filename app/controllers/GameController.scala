package controllers

import javax.inject.Inject

import akka.actor._
import akka.stream.Materializer
import io.github.fiifoo.scarl.game.{Game, OutConnection, OutMessage, Player}
import models.{Actions, Data, GenerateBubble, OutMessages}
import play.Environment
import play.api.libs.json.JsValue
import play.api.libs.streams._
import play.api.mvc._

class GameController @Inject()(implicit system: ActorSystem, materializer: Materializer, environment: Environment) extends Controller {

  def index = Action {
    val assets = if (environment.isDev) {
      "http://localhost:80"
    } else {
      routes.Assets.versioned("").toString
    }

    Ok(views.html.index(assets))
  }

  def socket = WebSocket.accept[JsValue, JsValue] { _ =>
    ActorFlow.actorRef(out => WebSocketActor.props(out))
  }

  object WebSocketActor {
    def props(out: ActorRef) = Props(new WebSocketActor(out))
  }

  class WebSocketActor(out: ActorRef) extends Actor {
    val factions = Data.factions
    val kinds = Data.kinds
    val templates = Data.templates

    val (s, creature) = GenerateBubble(factions, kinds, templates)

    val player = new Player(creature)
    val connection = new OutConnection(player, send)
    val game = new Game(connection, player, s)

    def receive = {
      case json: JsValue => game.receive(Actions.fromJson(json))
    }

    def send = (data: OutMessage) => {
      out ! OutMessages.toJson(data)
    }
  }

}
