package models.json

import io.github.fiifoo.scarl.core.item.Discover
import play.api.libs.json._

object JsonItemDiscover {

  import JsonBase.polymorphicObjectFormat

  lazy val discoverFormat: Format[Discover] = polymorphicObjectFormat({
    case "Discover.Everyone" => Discover.Everyone
    case "Discover.Nobody" => Discover.Nobody
    case "Discover.Triggerer" => Discover.Triggerer
  })
}
