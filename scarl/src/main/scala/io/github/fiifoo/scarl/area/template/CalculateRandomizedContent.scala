package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.area.feature.Utils
import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.area.template.ContentSelection._
import io.github.fiifoo.scarl.area.template.ContentSource.ItemSource
import io.github.fiifoo.scarl.area.template.RandomizedContentSource.Entrances
import io.github.fiifoo.scarl.area.template.Template.Result
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.math.Distribution.Uniform
import io.github.fiifoo.scarl.world.WorldAssets

import scala.util.Random


object CalculateRandomizedContent {

  def apply(source: RandomizedContentSource,
            shapeResult: Shape.Result,
            subResults: Map[Location, Result]
           )(assets: WorldAssets,
             area: Area,
             random: Random
           ): Result = {
    val subEntrances = (subResults map (x => {
      val (location, subResult) = x

      subResult.entrances map location.add
    })).toSet.flatten

    val contained = CalculateUtils.templateContainedLocations(shapeResult, subResults)
    val entranceResults = calculateEntrances(source.entrances)(assets, area, shapeResult.entranceCandidates, random)
    val routeResults = calculateRoutes(source.fill)(entranceResults, subEntrances, contained)
    val wallResults = calculateBorderWalls(source.border)(shapeResult.border, entranceResults) ++
      calculateFilledWalls(source.fill)(contained, routeResults)

    val contentResult = CalculateContent(
      assets = assets,
      area = area,
      shape = shapeResult,
      target = FixedContent(walls = wallResults),
      locations = contained,
      entrances = entranceResults,
      subEntrances = subEntrances,
      conduits = source.conduitLocations,
      features = source.features,
      terrain = source.terrain,
      random = random
    )

    Result(
      shape = shapeResult,
      templates = subResults,
      entrances = entranceResults.keys.toSet,
      content = contentResult
    )
  }

  def calculateEntrances(entrances: Entrances)
                        (assets: WorldAssets,
                         area: Area, locations: Set[Location],
                         random: Random
                        ): Map[Location, DoorSelection] = {
    val selection = entrances.door.getOrElse(ThemeDoor())
    val distribution = Uniform(entrances.min, entrances.max)
    val source = ItemSource(selection, distribution)

    Utils.randomUniqueSelectionLocations(locations, List(source), Map(), random)
  }

  def calculateRoutes(fill: Option[WallSelection])
                     (entranceResults: Map[Location, _],
                      subEntrances: Set[Location],
                      contained: Set[Location],
                     ): Set[Location] = {
    if (fill.isEmpty) {
      return Set()
    }

    val shouldConnect = entranceResults.keys.toSet ++ subEntrances

    CalculateRoutes(shouldConnect, contained)
  }

  def calculateBorderWalls(border: Option[WallSelection])
                          (borders: Set[Location],
                           entranceResults: Map[Location, _]
                          ): Map[Location, WallSelection] = {
    border map (border => borders
      .filterNot(entranceResults.isDefinedAt)
      .map((_, border))
      .toMap
      ) getOrElse Map()
  }

  def calculateFilledWalls(fill: Option[WallSelection])
                          (contained: Set[Location],
                           routeResults: Set[Location]
                          ): Map[Location, WallSelection] = {
    fill map (fill => contained
      .diff(routeResults)
      .map((_, fill))
      .toMap
      ) getOrElse Map()
  }
}
