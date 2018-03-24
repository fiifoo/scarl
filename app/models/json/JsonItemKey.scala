package models.json

import io.github.fiifoo.scarl.core.item._
import play.api.libs.json._

object JsonItemKey {

  import JsonBase.{mapReads, polymorphicTypeFormat, stringIdFormat}

  lazy private implicit val privateKeyFormat: Format[PrivateKey] = Json.format[PrivateKey]

  lazy implicit val sharedKeyFormat: Format[SharedKey] = Json.format[SharedKey]

  lazy val keyFormat: Format[Key] = polymorphicTypeFormat(
    data => {
      case "PrivateKey" => data.as[PrivateKey]
      case "SharedKey" => data.as[SharedKey]
    }, {
      case key: PrivateKey => privateKeyFormat.writes(key)
      case key: SharedKey => sharedKeyFormat.writes(key)
    }
  )

  lazy implicit val keyKindIdFormat: Format[KeyKindId] = stringIdFormat(_.value, KeyKindId.apply)
  lazy implicit val keyKindFormat: Format[KeyKind] = Json.format[KeyKind]

  lazy val keyKindMapReads: Reads[Map[KeyKindId, KeyKind]] = mapReads
}
