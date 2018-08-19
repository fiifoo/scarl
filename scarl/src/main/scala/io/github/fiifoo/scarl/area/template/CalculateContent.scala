package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.area.feature.Feature
import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.area.template.ContentSelection.{FixedItem, FixedTerrain}
import io.github.fiifoo.scarl.area.template.RandomizedTemplate.ConduitLocations
import io.github.fiifoo.scarl.area.template.Template.ResultContent
import io.github.fiifoo.scarl.core.Tag
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
            subEntrances: Set[Location],
            conduits: ConduitLocations = ConduitLocations(),
            features: List[Feature],
            terrain: Option[TerrainKindId],
            random: Random
           ): ResultContent = {

    val entranceItems = entrances map (entrance => {
      val (location, item) = entrance
      val selection = FixedItem(item)

      if (target.items.isDefinedAt(location)) {
        (location, selection :: target.items(location))
      } else {
        (location, List(selection))
      }
    })

    val conduitLocations = CalculateUtils.randomUniqueLocations(
      locations -- target.restrictedLocations -- target.walls.keys -- target.gatewayLocations -- target.conduitLocations.keys,
      conduits.min,
      conduits.max,
      random
    ).map(x => x -> conduits.tag).toMap

    var fixed = target.copy(
      conduitLocations = target.conduitLocations ++ conduitLocations,
      items = target.items ++ entranceItems
    )

    fixed = (features foldLeft fixed) ((content, feature) => {
      feature(assets, area, shape, content, locations, entrances.keySet, subEntrances, random)
    })

    val defaultTerrain = terrain getOrElse assets.themes(area.theme).terrain
    val defaultTerrains = (locations filterNot fixed.terrains.isDefinedAt map (location => {
      (location, FixedTerrain(defaultTerrain))
    })).toMap

    fixed = fixed.copy(
      terrains = fixed.terrains ++ defaultTerrains
    )

    def applySelection[T](selection: ContentSelection[T]): (T, Set[Tag]) = {
      val result = selection.apply(assets, area, random)

      if (result.isEmpty) {
        throw new CalculateFailedException
      }

      (result.get, selection.tags)
    }

    ResultContent(
      conduitLocations = fixed.conduitLocations,
      creatures = fixed.creatures mapValues applySelection,
      gatewayLocations = fixed.gatewayLocations,
      items = fixed.items mapValues (_ map applySelection),
      machinery = fixed.machinery,
      restrictedLocations = fixed.restrictedLocations,
      terrains = fixed.terrains mapValues applySelection,
      walls = fixed.walls mapValues applySelection,
      widgets = fixed.widgets mapValues applySelection
    )
  }
}
