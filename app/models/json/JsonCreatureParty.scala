package models.json

import io.github.fiifoo.scarl.core.creature.Party
import play.api.libs.json._

object JsonCreatureParty {
  lazy private implicit val creatureIdFormat = JsonCreature.creatureIdFormat

  lazy val creaturePartyFormat: Format[Party] = Json.format
}
