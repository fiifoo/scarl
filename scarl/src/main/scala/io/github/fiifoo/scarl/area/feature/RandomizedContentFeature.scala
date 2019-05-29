package io.github.fiifoo.scarl.area.feature

import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.area.feature.Utils._
import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.area.template.ContentSource._
import io.github.fiifoo.scarl.area.template.FixedContent
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.world.{ContentSourceCatalogue, ContentSourceCatalogueId, WorldAssets}

import scala.util.Random

case class RandomizedContentFeature(catalogues: List[ContentSourceCatalogueId] = List(),
                                    creatures: List[CreatureSource] = List(),
                                    items: List[ItemSource] = List(),
                                    widgets: List[WidgetSource] = List()
                                   ) extends Feature {

  def apply(assets: WorldAssets,
            area: Area,
            shape: Shape.Result,
            content: FixedContent,
            locations: Set[Location],
            entrances: Set[Location],
            subEntrances: Set[Location],
            random: Random
           ): FixedContent = {
    val free = freeLocations(assets, content, locations)
    val (creatureSources, itemSources, widgetSources) = getSources(assets)

    val creatures = randomUniqueSelectionLocations(
      locations = free,
      sources = creatureSources,
      existing = content.creatures,
      random = random
    )
    val widgets = randomUniqueSelectionLocations(
      locations = free,
      sources = widgetSources,
      existing = content.widgets,
      random = random
    )
    val items = randomSelectionLocations(
      locations = free -- widgets.keys,
      sources = itemSources,
      existing = content.items,
      random = random
    )

    content.copy(
      creatures = creatures,
      items = items,
      widgets = widgets
    )
  }

  private def getSources(assets: WorldAssets): (List[CreatureSource], List[ItemSource], List[WidgetSource]) = {
    val catalogue = ContentSourceCatalogue(
      ContentSourceCatalogueId(""),
      this.catalogues,
      this.creatures,
      this.items,
      this.widgets
    )

    catalogue(assets.catalogues.contentSources)
  }
}
