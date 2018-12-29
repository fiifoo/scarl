package models.json

import io.github.fiifoo.scarl.world.TransportCategory
import play.api.libs.json._

object JsonTransportCategory {

  import JsonBase.polymorphicObjectFormat

  lazy val transportCategoryFormat: Format[TransportCategory] = polymorphicObjectFormat({
    case "TransportCategory.Ship" => TransportCategory.Ship
  })
}
