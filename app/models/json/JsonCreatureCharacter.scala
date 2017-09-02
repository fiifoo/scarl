package models.json

import io.github.fiifoo.scarl.core.creature.Character
import play.api.libs.json._

object JsonCreatureCharacter {
  lazy private implicit val progressionIdFormat = JsonProgression.progressionIdFormat

  lazy val creatureCharacterFormat: Format[Character] = Json.format
}
