package models.json

import io.github.fiifoo.scarl.core.item.Mechanism
import io.github.fiifoo.scarl.mechanism.{CreateEntityMechanism, RemoveWallMechanism, UseDoorMechanism}
import play.api.libs.json._

object JsonMechanism {

  import JsonBase.polymorphicTypeFormat

  lazy private implicit val kindIdFormat = JsonKind.kindIdFormat
  lazy private implicit val createEntityFormat = Json.format[CreateEntityMechanism]
  lazy private implicit val removeWallFormat = Json.format[RemoveWallMechanism]
  lazy private implicit val useDoorFormat = Json.format[UseDoorMechanism]

  lazy implicit val mechanismFormat: Format[Mechanism] = polymorphicTypeFormat(
    data => {
      case "CreateEntityMechanism" => data.as[CreateEntityMechanism]
      case "RemoveWallMechanism" => data.as[RemoveWallMechanism]
      case "UseDoorMechanism" => data.as[UseDoorMechanism]
    }, {
      case mechanism: CreateEntityMechanism => createEntityFormat.writes(mechanism)
      case mechanism: RemoveWallMechanism => removeWallFormat.writes(mechanism)
      case mechanism: UseDoorMechanism => useDoorFormat.writes(mechanism)
    }
  )
}
