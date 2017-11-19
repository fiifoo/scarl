package models.json

import io.github.fiifoo.scarl.core.item.{Discover, DiscoverEveryone, DiscoverTriggerer}
import play.api.libs.json._

object JsonItemDiscover {

  import JsonBase.polymorphicObjectFormat

  lazy val discoverFormat: Format[Discover] = polymorphicObjectFormat({
    case "DiscoverTriggerer" => DiscoverTriggerer
    case "DiscoverEveryone" => DiscoverEveryone
  })
}
