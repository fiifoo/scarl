package models.json

import io.github.fiifoo.scarl.area.template.TemplateId
import io.github.fiifoo.scarl.world._
import models.json.JsonBase.{mapReads, weightedChoiceFormat}
import play.api.libs.json._

object JsonWorldCatalogues {

  import models.json.JsonBase.stringIdFormat

  lazy private implicit val templateIdFormat = JsonTemplate.templateIdFormat

  lazy implicit val contentSourceCatalogueIdFormat: Format[ContentSourceCatalogueId] = stringIdFormat(_.value, ContentSourceCatalogueId.apply)
  lazy implicit val templateCatalogueIdFormat: Format[TemplateCatalogueId] = stringIdFormat(_.value, TemplateCatalogueId.apply)
  lazy implicit val templateSourceCatalogueIdFormat: Format[TemplateSourceCatalogueId] = stringIdFormat(_.value, TemplateSourceCatalogueId.apply)

  implicitly(weightedChoiceFormat[TemplateId])
  lazy private implicit val creatureSourceReads = JsonContentSource.creatureSourceReads
  lazy private implicit val itemSourceReads = JsonContentSource.itemSourceReads
  lazy private implicit val templateSourceReads = JsonContentSource.templateSourceReads
  lazy private implicit val widgetSourceReads = JsonContentSource.widgetSourceReads

  lazy private implicit val contentSourceCatalogueReads: Reads[ContentSourceCatalogue] = Json.reads
  lazy private implicit val templateCatalogueReads: Reads[TemplateCatalogue] = Json.reads
  lazy private implicit val templateSourceCatalogueReads: Reads[TemplateSourceCatalogue] = Json.reads

  lazy private implicit val creaturesReads = JsonCatalogues.creaturesReads
  lazy private implicit val itemsReads = JsonCatalogues.itemsReads
  lazy private implicit val templateSourcesReads: Reads[Map[TemplateSourceCatalogueId, TemplateSourceCatalogue]] = mapReads
  lazy private implicit val templatesReads: Reads[Map[TemplateCatalogueId, TemplateCatalogue]] = mapReads
  lazy private implicit val terrainsReads = JsonCatalogues.terrainsReads
  lazy private implicit val wallsReads = JsonCatalogues.wallsReads
  lazy private implicit val widgetsReads = JsonCatalogues.widgetsReads

  lazy val worldCataloguesReads: Reads[WorldCatalogues] = Json.reads
}
