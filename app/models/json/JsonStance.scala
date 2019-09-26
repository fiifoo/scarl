package models.json

import io.github.fiifoo.scarl.core.creature.Stance
import io.github.fiifoo.scarl.status._
import play.api.libs.json._

object JsonStance {

  import JsonBase.polymorphicObjectFormat

  lazy implicit val stanceFormat: Format[Stance] = polymorphicObjectFormat({
    case "Stances.Aim" => Stances.Aim
  })
}
