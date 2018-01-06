package models.json

import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.game.statistics.Statistics
import play.api.libs.json._

object JsonStatistics {

  import JsonBase.mapFormat

  lazy private implicit val creatureKindIdFormat = JsonCreatureKind.creatureKindIdFormat
  implicitly(mapFormat[CreatureKindId, Int])

  lazy val statisticsFormat = Json.format[Statistics]
}
