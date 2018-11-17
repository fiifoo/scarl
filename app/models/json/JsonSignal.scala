package models.json

import io.github.fiifoo.scarl.core.entity.Signal
import io.github.fiifoo.scarl.core.geometry.Location
import play.api.libs.json._

object JsonSignal {

  import JsonBase.polymorphicObjectFormat

  lazy private implicit val locationFormat = Json.format[Location]
  lazy private implicit val factionIdFormat = JsonFaction.factionIdFormat

  lazy implicit val kindFormat: Format[Signal.Kind] = polymorphicObjectFormat({
    case "Signal.ConduitSignal" => Signal.ConduitSignal
    case "Signal.CreatureSignal" => Signal.CreatureSignal
    case "Signal.NoiseSignal" => Signal.NoiseSignal
  })

  lazy val signalFormat: Format[Signal] = Json.format
}
