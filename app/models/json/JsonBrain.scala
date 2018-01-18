package models.json

import io.github.fiifoo.scarl.core.ai.{Brain, Intention, Priority}
import io.github.fiifoo.scarl.core.entity.CreatureId
import play.api.libs.json._

object JsonBrain {

  import JsonBase.mapFormat

  lazy private implicit val strategyFormat = JsonStrategy.strategyFormat
  lazy private implicit val factionIdFormat = JsonFaction.factionIdFormat
  lazy private implicit val creatureIdFormat = JsonCreature.creatureIdFormat
  lazy private implicit val intentionFormat = JsonIntention.intentionFormat
  implicitly(mapFormat[CreatureId, List[(Intention, Priority.Value)]])

  lazy val brainFormat: Format[Brain] = Json.format
}
