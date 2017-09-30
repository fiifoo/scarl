package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.feature.RandomizedContentFeature.ItemSource
import io.github.fiifoo.scarl.area.feature.{Feature, RandomizedContentFeature}
import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.area.template.Template.Result
import io.github.fiifoo.scarl.area.theme.ContentSelection.FixedItem
import io.github.fiifoo.scarl.area.theme.Theme
import io.github.fiifoo.scarl.core.Distribution.Uniform
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.{Location, Rng}
import io.github.fiifoo.scarl.world.WorldAssets

import scala.util.Random

case class RandomizedTemplate(id: TemplateId,
                              shape: Shape,
                              templates: List[(TemplateId, Int, Int)] = List(),
                              entrances: List[(Option[ItemKindId], Int, Int)] = List(),
                              conduitLocations: (Int, Int) = (0, 0),
                              features: List[Feature] = List(),
                              border: Option[WallKindId] = None,
                              fill: Option[WallKindId] = None,
                              terrain: Option[TerrainKindId] = None
                             ) extends Template {

  def apply(assets: WorldAssets, theme: Theme, random: Random): Result = {
    val shapeResult = shape(random)
    val subResults = calculateSubTemplates(assets, theme, shapeResult, random)
    val contained = CalculateUtils.templateContainedLocations(shapeResult, subResults)
    val entranceResults = calculateEntrances(assets, theme, shapeResult.entranceCandidates, random)
    val routeResults = calculateRoutes(subResults, entranceResults, contained, random)
    val wallResults = calculateBorderWalls(shapeResult.border, entranceResults) ++ calculateFilledWalls(contained, routeResults)
    val contentResult = CalculateContent(
      assets = assets,
      theme = theme,
      locations = contained,
      target = FixedContent(walls = wallResults),
      entrances = entranceResults,
      conduits = conduitLocations,
      features = features,
      terrain = terrain,
      random = random
    )

    Result(
      shape = shapeResult,
      templates = subResults,
      entrances = entranceResults,
      content = contentResult,
    )
  }

  private def calculateSubTemplates(assets: WorldAssets,
                                    theme: Theme,
                                    shapeResult: Shape.Result,
                                    random: Random
                                   ): Map[Location, Result] = {

    val subResults = templates flatMap (definition => {
      val (sub, min, max) = definition
      val range = Rng.nextRange(random, min, max)

      range map (_ => sub(assets.templates)(assets, theme, random))
    })

    CalculateTemplateLocations(
      subResults,
      shapeResult,
      random
    )
  }

  private def calculateEntrances(assets: WorldAssets, theme: Theme, locations: Set[Location], random: Random): Map[Location, ItemKindId] = {
    val sources = this.entrances map (entrance => {
      val (item, min, max) = entrance
      val selection = FixedItem(item getOrElse theme.door)
      val distribution = Uniform(min, max)

      ItemSource(selection, distribution)
    })

    RandomizedContentFeature.randomUniqueElementLocations(assets, theme, locations, sources, Map(), random)
  }

  private def calculateRoutes(subResults: Map[Location, Result],
                              entranceResults: Map[Location, ItemKindId],
                              contained: Set[Location],
                              random: Random
                             ): Set[Location] = {
    if (fill.isEmpty) {
      return Set()
    }

    val fold = subResults.foldLeft(entranceResults.keys.toSet) _

    val shouldConnect = fold((shouldConnect, data) => {
      val (location, subResult) = data

      shouldConnect ++ (subResult.entrances.keys.toSet map location.add)
    })

    CalculateRoutes(shouldConnect, contained, random)
  }

  private def calculateBorderWalls(borders: Set[Location],
                                   entranceResults: Map[Location, ItemKindId]
                                  ): Map[Location, WallKindId] = {
    border map (border => borders
      .filterNot(entranceResults.isDefinedAt)
      .map((_, border))
      .toMap
      ) getOrElse Map()
  }

  private def calculateFilledWalls(contained: Set[Location],
                                   routeResults: Set[Location]
                                  ): Map[Location, WallKindId] = {
    fill map (fill => contained
      .diff(routeResults)
      .map((_, fill))
      .toMap
      ) getOrElse Map()
  }
}
