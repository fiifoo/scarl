package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.template.Template.{FixedContent, RandomizedContent}
import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.kind.{ItemKindId, TerrainKindId}

import scala.util.Random

object CalculateContent {

  def apply(locations: Set[Location],
            source: RandomizedContent,
            target: FixedContent,
            entrances: Map[Location, Option[ItemKindId]],
            terrain: Option[TerrainKindId],
            random: Random
           ): FixedContent = {

    val entranceItems = entrances filter (_._2.isDefined) map (entrance => {
      val (location, item) = entrance

      if (target.items.isDefinedAt(location)) {
        (location, item.get :: target.items(location))
      } else {
        (location, List(item.get))
      }
    })

    val noWalls = locations -- target.walls.keys

    val creatures = CalculateUtils.randomUniqueElementLocations(
      noWalls,
      source.creatures,
      target.creatures,
      random
    )
    val items = CalculateUtils.randomElementLocations(
      noWalls,
      source.items,
      entranceItems,
      random
    )
    val terrains = CalculateUtils.randomUniqueElementLocations(
      noWalls -- target.terrains.keys,
      source.terrains,
      target.terrains,
      random
    )
    val widgets = CalculateUtils.randomUniqueElementLocations(
      noWalls,
      source.widgets,
      target.widgets,
      random
    )

    val defaultTerrains = terrain map (terrain => {
      (noWalls filterNot terrains.isDefinedAt map (location => {
        (location, terrain)
      })).toMap
    }) getOrElse Map()

    target.copy(
      creatures = target.creatures ++ creatures,
      items = target.items ++ items,
      terrains = target.terrains ++ terrains ++ defaultTerrains,
      widgets = target.widgets ++ widgets
    )
  }
}
