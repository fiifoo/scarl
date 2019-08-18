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

trait CategorizedListCatalogueId

trait CategorizedListCatalogue[Id <: CategorizedListCatalogueId, Category, T] {
  val id: Id
  val subs: List[Id]
  val content: Map[Category, List[T]]

  def apply(catalogues: Map[Id, CategorizedListCatalogue[Id, Category, T]]): Map[Category, List[T]] = {
    val contents = this.content :: (subs flatMap catalogues.get map (_.apply(catalogues)))

    contents.flatten groupBy (_._1) transform ((_, x) => x flatMap (_._2))
  }
}

case class CreatureCatalogueId(value: String) extends CategorizedListCatalogueId

case class CreatureCatalogue(id: CreatureCatalogueId,
                             subs: List[CreatureCatalogueId] = List(),
                             content: Map[CreatureKind.Category, List[WeightedChoice[CreatureKindId]]] = Map()
                            ) extends CategorizedListCatalogue[CreatureCatalogueId, CreatureKind.Category, WeightedChoice[CreatureKindId]]

case class ItemCatalogueId(value: String) extends CategorizedListCatalogueId

case class ItemCatalogue(id: ItemCatalogueId,
                         subs: List[ItemCatalogueId] = List(),
                         content: Map[ItemKind.Category, List[WeightedChoice[ItemKindId]]] = Map()
                        ) extends CategorizedListCatalogue[ItemCatalogueId, ItemKind.Category, WeightedChoice[ItemKindId]]

case class TerrainCatalogueId(value: String) extends CategorizedListCatalogueId

case class TerrainCatalogue(id: TerrainCatalogueId,
                            subs: List[TerrainCatalogueId] = List(),
                            content: Map[TerrainKind.Category, List[WeightedChoice[TerrainKindId]]] = Map()
                           ) extends CategorizedListCatalogue[TerrainCatalogueId, TerrainKind.Category, WeightedChoice[TerrainKindId]]

case class WallCatalogueId(value: String) extends CategorizedListCatalogueId

case class WallCatalogue(id: WallCatalogueId,
                         subs: List[WallCatalogueId] = List(),
                         content: Map[WallKind.Category, List[WeightedChoice[WallKindId]]] = Map()
                        ) extends CategorizedListCatalogue[WallCatalogueId, WallKind.Category, WeightedChoice[WallKindId]]

case class WidgetCatalogueId(value: String) extends CategorizedListCatalogueId

case class WidgetCatalogue(id: WidgetCatalogueId,
                           subs: List[WidgetCatalogueId] = List(),
                           content: Map[WidgetKind.Category, List[WeightedChoice[WidgetKindId]]] = Map()
                          ) extends CategorizedListCatalogue[WidgetCatalogueId, WidgetKind.Category, WeightedChoice[WidgetKindId]]
