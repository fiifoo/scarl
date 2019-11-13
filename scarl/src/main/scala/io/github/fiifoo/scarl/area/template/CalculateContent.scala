package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.feature.Feature
import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.area.template.ContentSelection._
import io.github.fiifoo.scarl.area.template.RandomizedContentSource.ConduitLocations
import io.github.fiifoo.scarl.area.template.Template.ResultContent
import io.github.fiifoo.scarl.area.theme.ThemeId
import io.github.fiifoo.scarl.core.Tag
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.world.WorldAssets

import scala.util.Random

object CalculateContent {

  def apply(assets: WorldAssets,
            theme: ThemeId,
            shape: Shape.Result,
            target: FixedContent,
            locations: Set[Location],
            entrances: Map[Location, DoorSelection],
            subEntrances: Set[Location],
            conduits: ConduitLocations = ConduitLocations(),
            features: List[Feature],
            terrain: Option[TerrainSelection],
            random: Random
           ): ResultContent = {

    val entranceItems = entrances map (entrance => {
      val (location, selection) = entrance

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
      feature(assets, theme, shape, content, locations, entrances.keySet, subEntrances, random)
    })

    val defaultTerrain = terrain getOrElse ThemeTerrain()
    val defaultTerrains = ((shape.border ++ locations) filterNot fixed.terrains.isDefinedAt map (location => {
      (location, defaultTerrain)
    })).toMap

    fixed = fixed.copy(
      terrains = fixed.terrains ++ defaultTerrains
    )

    def applyKeyedSelection[K, T](k: K, selection: ContentSelection[T]): (T, Set[Tag]) = {
      applySelection(selection)
    }

    def applySelection[T](selection: ContentSelection[T]): (T, Set[Tag]) = {
      val result = selection.apply(assets, theme, random)

      if (result.isEmpty) {
        throw new CalculateFailedException
      }

      (result.get, selection.tags)
    }

    ResultContent(
      conduitLocations = fixed.conduitLocations,
      creatures = fixed.creatures transform applyKeyedSelection,
      gatewayLocations = fixed.gatewayLocations,
      items = fixed.items transform ((_, items) => items map applySelection),
      machinery = fixed.machinery,
      restrictedLocations = fixed.restrictedLocations,
      terrains = fixed.terrains transform applyKeyedSelection,
      walls = fixed.walls transform applyKeyedSelection,
      widgets = fixed.widgets transform applyKeyedSelection
    )
  }
}
