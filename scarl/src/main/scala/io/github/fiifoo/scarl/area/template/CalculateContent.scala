package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.area.feature.Feature
import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind.{ItemKindId, TerrainKindId}
import io.github.fiifoo.scarl.world.WorldAssets

import scala.util.Random

object CalculateContent {

  def apply(assets: WorldAssets,
            area: Area,
            shape: Shape.Result,
            target: FixedContent,
            locations: Set[Location],
            entrances: Map[Location, ItemKindId],
            conduits: (Int, Int) = (0, 0),
            features: List[Feature],
            terrain: Option[TerrainKindId],
            random: Random
           ): FixedContent = {

    val entranceItems = entrances map (entrance => {
      val (location, item) = entrance

      if (target.items.isDefinedAt(location)) {
        (location, item :: target.items(location))
      } else {
        (location, List(item))
      }
    })

    val conduitLocations = CalculateUtils.randomUniqueLocations(
      locations -- target.walls.keys -- target.gatewayLocations,
      conduits,
      target.conduitLocations,
      random
    )

    var result = target.copy(
      conduitLocations = target.conduitLocations ++ conduitLocations,
      items = target.items ++ entranceItems
    )

    result = (features foldLeft result) ((content, feature) => {
      feature(assets, area, shape, content, locations, entrances.keySet, random)
    })

    val defaultTerrain = terrain getOrElse assets.themes(area.theme).terrain
    val defaultTerrains = (locations filterNot result.terrains.isDefinedAt map (location => {
      (location, defaultTerrain)
    })).toMap

    result.copy(
      terrains = result.terrains ++ defaultTerrains
    )
  }
}
