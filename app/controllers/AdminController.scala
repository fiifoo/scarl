package controllers

import javax.inject.Inject

import dal.AssetsRepository
import models.admin.Models
import play.Environment
import play.api.mvc._

class AdminController @Inject()(gameAssets: AssetsRepository,
                                cc: ControllerComponents)
                               (environment: Environment) extends AbstractController(cc) {

  val assets = if (environment.isDev) {
    "http://localhost:81"
  } else {
    routes.Assets.versioned("").toString
  }

  val readonly = !environment.isDev

  def index = Action {
    val data = gameAssets.read()
    val models = Models()

    Ok(views.html.admin(
      assets = assets,
      data = data.toString,
      models = Models.writes.writes(models).toString,
      readonly = readonly,
    ))
  }

  def save = Action(parse.json) { request =>
    if (readonly) {
      throw new Exception("Dev environment only.")
    }

    gameAssets.write(request.body)

    Ok(request.body)
  }
}
