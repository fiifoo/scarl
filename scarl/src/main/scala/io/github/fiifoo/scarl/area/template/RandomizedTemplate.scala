package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.area.feature.{Feature, Utils}
import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.area.template.ContentSelection.FixedItem
import io.github.fiifoo.scarl.area.template.ContentSource.{ItemSource, TemplateSource}
import io.github.fiifoo.scarl.area.template.Template.{Category, Result}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.math.Distribution.Uniform
import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.world.WorldAssets

import scala.util.Random

case class RandomizedTemplate(id: TemplateId,
                              shape: Shape,
                              category: Option[Category] = None,
                              power: Option[Int] = None,
                              border: Option[WallKindId] = None,
                              fill: Option[WallKindId] = None,
                              terrain: Option[TerrainKindId] = None,
                              templates: List[TemplateSource] = List(),
                              entrances: List[(Option[ItemKindId], Int, Int)] = List(),
                              conduitLocations: (Int, Int) = (0, 0),
                              features: List[Feature] = List(),
                             ) extends Template {

  def apply(assets: WorldAssets, area: Area, random: Random): Result = {
    val shapeResult = shape(random)
    val subResults = calculateSubTemplates(assets, area, shapeResult, random)
    val contained = CalculateUtils.templateContainedLocations(shapeResult, subResults)
    val entranceResults = calculateEntrances(assets, area, shapeResult.entranceCandidates, random)
    val routeResults = calculateRoutes(subResults, entranceResults, contained, random)
    val wallResults = calculateBorderWalls(shapeResult.border, entranceResults) ++ calculateFilledWalls(contained, routeResults)
    val contentResult = CalculateContent(
      assets = assets,
      area = area,
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
                                    area: Area,
                                    shape: Shape.Result,
                                    random: Random
                                   ): Map[Location, Result] = {
    def calculate(source: TemplateSource): Iterable[Result] = {
      val sub = source(assets, area, random)
      val range = Rng.nextRange(random, source.distribution)

      range map (_ => sub(assets.templates)(assets, area, random))
    }

    val (optional, required) = this.templates partition (_.optional)

    CalculateTemplateLocations(
      required flatMap calculate,
      optional flatMap calculate,
      shape,
      random
    )
  }

  private def calculateEntrances(assets: WorldAssets, area: Area, locations: Set[Location], random: Random): Map[Location, ItemKindId] = {
    val sources = this.entrances map (entrance => {
      val (item, min, max) = entrance
      val selection = FixedItem(item getOrElse assets.themes(area.theme).door)
      val distribution = Uniform(min, max)

      ItemSource(selection, distribution)
    })

    Utils.randomUniqueElementLocations(assets, area, locations, sources, Map(), random)
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
