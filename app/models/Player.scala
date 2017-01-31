package models

import io.github.fiifoo.scarl.core.entity.{CreatureId, TerrainId, WallId}
import io.github.fiifoo.scarl.core.{Location, Selectors, State}
import io.github.fiifoo.scarl.geometry.Fov
import models.Entities.Fov.LocationEntities
import play.api.libs.json._

class Player(send: JsValue => Unit) {

  val creature = CreatureId(1)
  val _range = 10 // from creature

  var previousFov: Map[Location, LocationEntities] = Map()

  def receive(s: State): Unit = {

    val data: Map[String, JsValue] = if (s.entities.isDefinedAt(creature)) {

      val (delta, shouldHide) = calculateFov(s)

      Map(
        "fov" -> JsObject(Map(
          "delta" -> Entities.Fov.toJson(delta),
          "shouldHide" -> Entities.Fov.toJson(shouldHide)
        )),
        "player" -> Entities.toJson(CreatureId(1)(s))
      )
    } else {
      Map(
        "fov" -> JsObject(Map(
          "delta" -> JsArray(),
          "shouldHide" -> JsArray()
        )),
        "player" -> JsNull
      )
    }

    send(Json.toJson(data))
  }

  private def calculateFov(s: State): (Map[Location, LocationEntities], Iterable[Location]) = {
    val location = creature(s).location
    val fov = Fov(s)(location, _range)

    val getLocationEntities = Selectors.getLocationEntities(s) _

    val next: Map[Location, LocationEntities] = fov.map(location => {
      val entities = getLocationEntities(location)

      location -> LocationEntities(
        entities collectFirst { case c: CreatureId => c(s) },
        entities collectFirst { case t: TerrainId => t(s) },
        entities collectFirst { case w: WallId => w(s) }
      )
    }).toMap

    val delta = next filterNot (x => previousFov.contains(x._1) && previousFov(x._1) == x._2)
    val shouldHide = (previousFov -- next.keys filter (_._2.creature.isDefined)).keys

    previousFov = next

    (delta, shouldHide)
  }
}
