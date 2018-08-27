package io.github.fiifoo.scarl.world

import io.github.fiifoo.scarl.area.template.ContentSource.{CreatureSource, ItemSource, WidgetSource}
import io.github.fiifoo.scarl.area.template.TemplateId
import io.github.fiifoo.scarl.core.assets._
import io.github.fiifoo.scarl.core.math.Rng.WeightedChoice

case class WorldCatalogues(contentSources: Map[ContentSourceCatalogueId, ContentSourceCatalogue] = Map(),
                           creatures: Map[CreatureCatalogueId, CreatureCatalogue] = Map(),
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

case class ContentSourceCatalogueId(value: String)

case class ContentSourceCatalogue(id: ContentSourceCatalogueId,
                                  subs: List[ContentSourceCatalogueId] = List(),
                                  creatures: List[CreatureSource] = List(),
                                  items: List[ItemSource] = List(),
                                  widgets: List[WidgetSource] = List(),
                                 ) {
  def apply(catalogues: Map[ContentSourceCatalogueId, ContentSourceCatalogue]
           ): (List[CreatureSource], List[ItemSource], List[WidgetSource]) = {

    (subs foldLeft(creatures, items, widgets)) ((x, sub) => {
      val y = catalogues(sub).apply(catalogues)

      (x._1 ::: y._1, x._2 ::: y._2, x._3 ::: y._3)
    })
  }
}

case class TemplateCatalogueId(value: String) extends ListCatalogueId

case class TemplateCatalogue(id: TemplateCatalogueId,
                             subs: List[TemplateCatalogueId] = List(),
                             items: List[WeightedChoice[TemplateId]] = List()
                            ) extends ListCatalogue[TemplateCatalogueId, WeightedChoice[TemplateId]]
