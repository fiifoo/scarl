package models.json

import io.github.fiifoo.scarl.area.{Area, AreaId}
import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.core.creature.Faction.Disposition
import io.github.fiifoo.scarl.core.creature.FactionId
import models.json.JsonBase.mapFormat
import play.api.libs.json._

object JsonArea {

  import JsonBase.{mapReads, stringIdFormat, tuple2Format}

  lazy private implicit val combatPowerCategoryFormat = JsonCombatPower.categoryFormat
  lazy private implicit val dispositionFormat = JsonFaction.dispositionFormat
  lazy private implicit val factionIdFormat = JsonFaction.factionIdFormat
  lazy private implicit val templateIdFormat = JsonTemplate.templateIdFormat
  lazy private implicit val themeIdFormat = JsonTheme.themeIdFormat

  lazy implicit val areaIdFormat: Format[AreaId] = stringIdFormat(_.value, AreaId.apply)

  implicitly(mapFormat[FactionId, Disposition])
  implicitly(mapFormat[FactionId, Map[FactionId, Disposition]])
  implicitly(mapReads[CombatPower.Category, (Int, Int)])

  lazy implicit val areaReads: Reads[Area] = Json.reads

  lazy val areaMapReads: Reads[Map[AreaId, Area]] = mapReads
}
