package io.github.fiifoo.scarl.area.feature

import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.area.feature.Utils._
import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.area.template.ContentSource._
import io.github.fiifoo.scarl.area.template.FixedContent
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.world.WorldAssets

import scala.util.Random

case class RandomizedContentFeature(creatures: List[CreatureSource] = List(),
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
    val free = freeLocations(content, locations)

    val creatures = randomUniqueElementLocations(
      assets = assets,
      area = area,
      locations = free,
      sources = this.creatures,
      existing = content.creatures,
      random = random
    )

    val widgets = randomUniqueElementLocations(
      assets = assets,
      area = area,
      locations = free,
      sources = this.widgets,
      existing = content.widgets,
      random = random
    )
    val items = randomElementLocations(
      assets = assets,
      area = area,
      locations = free -- widgets.keys,
      sources = this.items,
      existing = content.items,
      random = random
    )

    content.copy(
      creatures = creatures,
      items = items,
      widgets = widgets
    )
  }
}
