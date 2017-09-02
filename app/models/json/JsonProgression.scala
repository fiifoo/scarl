package models.json

import io.github.fiifoo.scarl.core.creature.Progression.{Requirements, Step}
import io.github.fiifoo.scarl.core.creature.{Progression, ProgressionId}
import play.api.libs.json._

object JsonProgression {

  import JsonBase.{mapReads, stringIdFormat}

  lazy private implicit val creatureStatsFormat = JsonCreatureStats.creatureStatsFormat
  lazy private implicit val requirementsReads = Json.reads[Requirements]
  lazy private implicit val stepReads = Json.reads[Step]

  lazy implicit val progressionIdFormat: Format[ProgressionId] = stringIdFormat(_.value, ProgressionId.apply)

  lazy implicit val progressionReads: Reads[Progression] = Json.reads

  lazy val progressionMapReads: Reads[Map[ProgressionId, Progression]] = mapReads
}
