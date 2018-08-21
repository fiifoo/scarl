package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.area.feature.{Feature, Utils}
import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.area.template.ContentSelection.FixedItem
import io.github.fiifoo.scarl.area.template.ContentSource.{ItemSource, TemplateSource}
import io.github.fiifoo.scarl.area.template.RandomizedTemplate.{ConduitLocations, Entrances}
import io.github.fiifoo.scarl.area.template.Template.{Category, Result}
import io.github.fiifoo.scarl.core.Tag
import io.github.fiifoo.scarl.core.geometry.{Location, Rotation}
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.math.Distribution.Uniform
import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.world.WorldAssets

import scala.util.Random

object RandomizedTemplate {

  case class Entrances(min: Int = 0,
                       max: Int = 0,
                       door: Option[ItemKindId] = None
                      )

  case class ConduitLocations(min: Int = 0,
                              max: Int = 0,
                              tag: Option[Tag] = None
                             )

}

case class RandomizedTemplate(id: TemplateId,
                              shape: Shape,
                              category: Option[Category] = None,
                              power: Option[Int] = None,
                              border: Option[WallKindId] = None,
                              fill: Option[WallKindId] = None,
                              terrain: Option[TerrainKindId] = None,
                              templates: List[TemplateSource] = List(),
                              entrances: Entrances = Entrances(),
                              conduitLocations: ConduitLocations = ConduitLocations(),
                              features: List[Feature] = List(),
                             ) extends Template {

  def apply(assets: WorldAssets, area: Area, random: Random): Result = {
    val shapeResult = shape(random)
    val subResults = calculateSubTemplates(assets, area, shapeResult, random)
    val subEntrances = (subResults map (x => {
      val (location, subResult) = x

      subResult.entrances.keys map location.add
    })).toSet.flatten

    val contained = CalculateUtils.templateContainedLocations(shapeResult, subResults)
    val entranceResults = calculateEntrances(assets, area, shapeResult.entranceCandidates, random)
    val routeResults = calculateRoutes(entranceResults, subEntrances, contained, random)
    val wallResults = calculateBorderWalls(shapeResult.border, entranceResults) ++ calculateFilledWalls(contained, routeResults)
    val contentResult = CalculateContent.apply(
      assets = assets,
      area = area,
      shape = shapeResult,
      target = FixedContent(walls = wallResults),
      locations = contained,
      entrances = entranceResults,
      subEntrances = subEntrances,
      conduits = conduitLocations,
      features = features,
      terrain = terrain,
      random = random
    )

    Result(
      shape = shapeResult,
      templates = subResults,
      entrances = entranceResults,
      content = contentResult
    )
  }

  private def calculateSubTemplates(assets: WorldAssets,
                                    area: Area,
                                    shape: Shape.Result,
                                    random: Random
                                   ): Map[Location, Result] = {
    def calculate(source: TemplateSource): Iterable[Result] = {
      val result = source.selection.apply(assets, area, random) map (sub => {
        val range = Rng.nextRange(random, source.distribution)

        range map (_ => {
          val result = sub(assets.templates)(assets, area, random)
          val rotation = Rotation(random, result.shape.outerWidth, result.shape.outerHeight).reverse

          result.rotate(rotation)
        })
      })

      if (result.isEmpty) {
        throw new CalculateFailedException
      }

      result.get
    }

    val (optional, required) = this.templates partition (_.optional)

    CalculateTemplateLocations(
      required flatMap calculate,
      optional flatMap calculate,
      shape,
      random
    )
  }

  private def calculateEntrances(assets: WorldAssets,
                                 area: Area, locations: Set[Location],
                                 random: Random
                                ): Map[Location, ItemKindId] = {
    val selection = FixedItem(this.entrances.door getOrElse assets.themes(area.theme).door)
    val distribution = Uniform(this.entrances.min, this.entrances.max)
    val source = ItemSource(selection, distribution)

    Utils.randomUniqueElementLocations(assets, area, locations, List(source), Map(), random)
  }

  private def calculateRoutes(entranceResults: Map[Location, ItemKindId],
                              subEntrances: Set[Location],
                              contained: Set[Location],
                              random: Random
                             ): Set[Location] = {
    if (fill.isEmpty) {
      return Set()
    }

    val shouldConnect = entranceResults.keys.toSet ++ subEntrances

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
