package models.json

import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.game.player.PlayerFov
import play.api.libs.json._

object JsonPlayerFov {
  lazy private implicit val locationWrites = Json.writes[Location]
  lazy private implicit val locationEntitiesWrites = JsonLocationEntities.locationEntitiesWrites

  lazy val playerFovWrites: Writes[PlayerFov] = fov =>
    JsObject(Map(
      "delta" -> Json.toJson(fov.delta),
      "shouldHide" -> Json.toJson(fov.shouldHide)
    ))
}
