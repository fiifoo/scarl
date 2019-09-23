package controllers

import dal.{AdminUiRepository, AssetsRepository, SimulationsRepository}
import javax.inject.Inject
import models.Simulations
import models.admin.{Models, Summary}
import play.Environment
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class AdminController @Inject()(adminUiRepository: AdminUiRepository,
                                gameAssets: AssetsRepository,
                                gameSimulations: SimulationsRepository,
                                cc: ControllerComponents
                               )(
                                 implicit ec: ExecutionContext,
                                 environment: Environment
                               ) extends AbstractController(cc) {

  val saveMaxSize = 1024 * 1000 * 10 // 10MB

  val assets = if (environment.isDev) {
    "http://localhost:8081"
  } else {
    routes.Assets.versioned("").toString
  }

  val readonly = !environment.isDev

  def index = Action {
    val data = gameAssets.read()
    val ui = adminUiRepository.read()

    val models = Models()

    Ok(views.html.admin(
      assets = assets,
      data = data.toString,
      models = Models.writes.writes(models).toString,
      readonly = readonly,
      ui = ui map (_.toString())
    ))
  }

  def save = Action.async(parse.json(saveMaxSize)) { request =>
    if (readonly) {
      throw new Exception("Dev environment only.")
    }

    gameAssets.write(request.body)

    Future {
      NoContent
    }
  }

  def saveUi = Action.async(parse.json) { request =>
    if (readonly) {
      throw new Exception("Dev environment only.")
    }

    adminUiRepository.write(request.body)

    Future {
      NoContent
    }
  }

  def simulate = Action {
    if (readonly) {
      throw new Exception("Dev environment only.")
    }

    val simulations = Simulations(gameAssets.readObject())
    gameSimulations.write(simulations)

    NoContent
  }

  def summary = Action {
    val summary = try {
      Summary(gameAssets.build())
    } catch {
      case _: Exception => Summary(valid = false)
    }

    Ok(summary.toJson.toString)
  }
}
