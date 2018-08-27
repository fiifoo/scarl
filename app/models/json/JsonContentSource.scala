package models.json

import io.github.fiifoo.scarl.area.template.ContentSource
import play.api.libs.json._

object JsonContentSource {
  lazy private implicit val distributionFormat = JsonDistribution.distributionFormat

  lazy private implicit val creatureSelectionReads = JsonContentSelection.creatureSelectionReads
  lazy private implicit val itemSelectionReads = JsonContentSelection.itemSelectionReads
  lazy private implicit val terrainSelectionReads = JsonContentSelection.terrainSelectionReads
  lazy private implicit val templateSelectionReads = JsonContentSelection.templateSelectionReads
  lazy private implicit val wallSelectionReads = JsonContentSelection.wallSelectionReads
  lazy private implicit val widgetSelectionReads = JsonContentSelection.widgetSelectionReads

  lazy val creatureSourceReads: Reads[ContentSource.CreatureSource] = Json.reads
  lazy val itemSourceReads: Reads[ContentSource.ItemSource] = Json.reads
  lazy val templateSourceReads: Reads[ContentSource.TemplateSource] = Json.reads
  lazy val widgetSourceReads: Reads[ContentSource.WidgetSource] = Json.reads
}
