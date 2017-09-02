package models.json

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.world.ConduitId
import io.github.fiifoo.scarl.world.{Conduit, WorldAssets, WorldState}
import play.api.libs.json._

object JsonWorldState {

  import JsonBase.{emptyFormat, mapFormat}

  // reset from game data
  lazy private implicit val assetsFormat = emptyFormat(WorldAssets())

  lazy private implicit val areaIdFormat = JsonArea.areaIdFormat
  lazy private implicit val conduitFormat = JsonConduit.conduitFormat
  lazy private implicit val conduitIdFormat = JsonConduit.conduitIdFormat
  lazy private implicit val stateFormat = JsonState.stateFormat

  implicitly(mapFormat[ConduitId, Conduit])
  implicitly(mapFormat[AreaId, State])

  lazy val worldStateFormat = Json.format[WorldState]
}
