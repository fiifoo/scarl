package models.admin

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.area.template.{TemplateId, TestTemplates}
import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.world.WorldAssets
import models.json._
import play.api.libs.json.{JsValue, Json}

case class Summary(valid: Boolean = true,
                   combatPower: CombatPower = CombatPower(),
                   templates: Map[AreaId, Map[TemplateId, Int]] = Map(),
                  ) {
  def toJson: JsValue = {
    Summary.writes.writes(this)
  }
}

object Summary {
  def apply(assets: WorldAssets): Summary = {
    Summary(
      combatPower = assets.combatPower,
      templates = TestTemplates(assets)
    )
  }

  lazy private implicit val areaIdFormat = JsonArea.areaIdFormat
  lazy private implicit val combatPowerWrites = JsonCombatPower.combatPowerWrites
  lazy private implicit val templateIdFormat = JsonTemplate.templateIdFormat

  import models.json.JsonBase.mapFormat

  implicitly(mapFormat[TemplateId, Int])
  implicitly(mapFormat[AreaId, Map[TemplateId, Int]])

  lazy val writes = Json.writes[Summary]
}
