package io.github.fiifoo.scarl.world

import io.github.fiifoo.scarl.area.template.TemplateId
import io.github.fiifoo.scarl.core.assets._
import io.github.fiifoo.scarl.core.math.Rng.WeightedChoice

case class WorldCatalogues(creatures: Map[CreatureCatalogueId, CreatureCatalogue] = Map(),
                           items: Map[ItemCatalogueId, ItemCatalogue] = Map(),
                           templates: Map[TemplateCatalogueId, TemplateCatalogue] = Map(),
                           terrains: Map[TerrainCatalogueId, TerrainCatalogue] = Map(),
                           walls: Map[WallCatalogueId, WallCatalogue] = Map(),
                           widgets: Map[WidgetCatalogueId, WidgetCatalogue] = Map()
                          ) {
  def instance(): Catalogues = {
    Catalogues(
      creatures,
      items,
      terrains,
      walls,
      widgets
    )
  }
}

case class TemplateCatalogueId(value: String) extends ListCatalogueId

case class TemplateCatalogue(id: TemplateCatalogueId,
                             subs: List[TemplateCatalogueId] = List(),
                             items: List[WeightedChoice[TemplateId]] = List()
                            ) extends ListCatalogue[TemplateCatalogueId, WeightedChoice[TemplateId]]
