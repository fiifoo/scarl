package controllers

import javax.inject.Inject

import akka.actor._
import akka.stream.Materializer
import dal.GameRepository
import game.GameInstance
import models.message._
import models.{DataManager, Game}
import play.Environment
import play.api.libs.json.JsValue
import play.api.libs.streams._
import play.api.mvc._

import scala.concurrent.ExecutionContext

class GameController @Inject()(dataManager: DataManager,
                               games: GameRepository,
                               cc: ControllerComponents
                              )(
                                implicit ec: ExecutionContext,
                                system: ActorSystem,
                                mat: Materializer,
                                environment: Environment
                              ) extends AbstractController(cc) {

  val assets = if (environment.isDev) {
    "http://localhost:80"
  } else {
    routes.Assets.versioned("").toString
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
    games.list() foreach sendGames

    var instance: Option[GameInstance] = None

    def receive = {
      case json: JsValue =>
        if (instance.isEmpty) {
          createInstance(json)
        } else {
          instance foreach (_.receive(json))
        }
    }

    override def postStop() = {
      instance foreach (_.stop())
    }

    private def sendGames(games: Seq[Game]) = {
      out ! OutMessage.writes.writes(Games(games))
    }

    private def sendCreateGameFailed(games: Seq[Game]) = {
      out ! OutMessage.writes.writes(CreateGameFailed(games))
    }

    private def createInstance(json: JsValue) = {
      val assets = dataManager.build()

      CreateGameMessage.reads.reads(json) foreach (message => {
        GameInstance(games, assets, message, out) foreach (created => {
          instance = created

          if (created.isEmpty) {
            games.list() foreach sendCreateGameFailed
          }
        })
      })
    }
  }

}
