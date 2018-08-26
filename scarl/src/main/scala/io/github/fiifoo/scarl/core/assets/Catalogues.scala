package io.github.fiifoo.scarl.core.assets

import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.math.Rng.WeightedChoice

case class Catalogues(creatures: Map[CreatureCatalogueId, CreatureCatalogue] = Map(),
                      items: Map[ItemCatalogueId, ItemCatalogue] = Map(),
                      terrains: Map[TerrainCatalogueId, TerrainCatalogue] = Map(),
                      walls: Map[WallCatalogueId, WallCatalogue] = Map(),
                      widgets: Map[WidgetCatalogueId, WidgetCatalogue] = Map(),
                     )

trait ListCatalogueId

trait ListCatalogue[Id <: ListCatalogueId, T] {
  val id: Id
  val subs: List[Id]
  val items: List[T]

  def apply(catalogues: Map[Id, ListCatalogue[Id, T]]): List[T] = {
    items ::: (subs flatMap catalogues.get flatMap (_.apply(catalogues)))
  }
}

case class CreatureCatalogueId(value: String) extends ListCatalogueId

case class CreatureCatalogue(id: CreatureCatalogueId,
                             subs: List[CreatureCatalogueId] = List(),
                             items: List[WeightedChoice[CreatureKindId]] = List()
                            ) extends ListCatalogue[CreatureCatalogueId, WeightedChoice[CreatureKindId]]

case class ItemCatalogueId(value: String) extends ListCatalogueId

case class ItemCatalogue(id: ItemCatalogueId,
                         subs: List[ItemCatalogueId] = List(),
                         items: List[WeightedChoice[ItemKindId]] = List()
                        ) extends ListCatalogue[ItemCatalogueId, WeightedChoice[ItemKindId]]

case class TerrainCatalogueId(value: String) extends ListCatalogueId

case class TerrainCatalogue(id: TerrainCatalogueId,
                            subs: List[TerrainCatalogueId] = List(),
                            items: List[WeightedChoice[TerrainKindId]] = List()
                           ) extends ListCatalogue[TerrainCatalogueId, WeightedChoice[TerrainKindId]]

case class WallCatalogueId(value: String) extends ListCatalogueId

case class WallCatalogue(id: WallCatalogueId,
                         subs: List[WallCatalogueId] = List(),
                         items: List[WeightedChoice[WallKindId]] = List()
                        ) extends ListCatalogue[WallCatalogueId, WeightedChoice[WallKindId]]

case class WidgetCatalogueId(value: String) extends ListCatalogueId

case class WidgetCatalogue(id: WidgetCatalogueId,
                           subs: List[WidgetCatalogueId] = List(),
                           items: List[WeightedChoice[WidgetKindId]] = List()
                          ) extends ListCatalogue[WidgetCatalogueId, WeightedChoice[WidgetKindId]]
