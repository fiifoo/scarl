package models.json

import io.github.fiifoo.scarl.core.world.GoalId
import io.github.fiifoo.scarl.world.Goal
import play.api.libs.json._

object JsonGoal {

  import JsonBase.{mapReads, stringIdFormat}

  lazy implicit val goalIdFormat: Format[GoalId] = stringIdFormat(_.value, GoalId.apply)

  lazy implicit val goalFormat: Format[Goal] = Json.format

  lazy val goalMapReads: Reads[Map[GoalId, Goal]] = mapReads
}
