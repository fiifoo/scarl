package models.json

import io.github.fiifoo.scarl.area.template.TemplateId
import io.github.fiifoo.scarl.world.{TemplateCatalogue, TemplateCatalogueId, WorldCatalogues}
import models.json.JsonBase.{mapReads, weightedChoiceFormat}
import play.api.libs.json._

object JsonWorldCatalogues {

  import models.json.JsonBase.stringIdFormat

  lazy private implicit val templateIdFormat = JsonTemplate.templateIdFormat

  lazy implicit val templateCatalogueIdFormat: Format[TemplateCatalogueId] = stringIdFormat(_.value, TemplateCatalogueId.apply)

  implicitly(weightedChoiceFormat[TemplateId])

  lazy private implicit val templateCatalogueReads: Reads[TemplateCatalogue] = Json.reads

  lazy private implicit val creaturesReads = JsonCatalogues.creaturesReads
  lazy private implicit val itemsReads = JsonCatalogues.itemsReads
  lazy private implicit val templatesReads: Reads[Map[TemplateCatalogueId, TemplateCatalogue]] = mapReads
  lazy private implicit val terrainsReads = JsonCatalogues.terrainsReads
  lazy private implicit val wallsReads = JsonCatalogues.wallsReads
  lazy private implicit val widgetsReads = JsonCatalogues.widgetsReads

  lazy val worldCataloguesReads: Reads[WorldCatalogues] = Json.reads
}
