package models.json

import io.github.fiifoo.scarl.core.creature.Missile
import io.github.fiifoo.scarl.core.creature.Missile.{Guidance, Guided, Smart}
import play.api.libs.json._

object JsonCreatureMissile {

  import JsonBase.polymorphicTypeFormat

  lazy implicit val guidanceFormat: Format[Guidance] = polymorphicTypeFormat(
    _ => {
      case "Guided" => Guided
      case "Smart" => Smart
    }, {
      _ => JsNull
    }
  )

  lazy val creatureMissileFormat: Format[Missile] = Json.format
}
