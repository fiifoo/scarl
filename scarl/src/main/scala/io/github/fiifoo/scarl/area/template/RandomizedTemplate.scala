package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.area.template.Template.{FixedContent, RandomizedContent, Result}
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.{Location, Rng}

import scala.util.Random

case class RandomizedTemplate(id: TemplateId,
                              shape: Shape,
                              templates: List[(TemplateId, Int, Int)] = List(),
                              entrances: List[(Option[ItemKindId], Int, Int)] = List(),
                              content: RandomizedContent = RandomizedContent(),
                              border: Option[WallKindId] = None,
                              fill: Option[WallKindId] = None,
                              terrain: Option[TerrainKindId] = None
                             ) extends Template {

  def apply(t: Map[TemplateId, Template], random: Random): Result = {
    val shapeResult = shape(random)
    val subResults = calculateSubTemplates(t, shapeResult, random)
    val contained = CalculateUtils.templateContainedLocations(shapeResult, subResults)
    val entranceResults = calculateEntrances(shapeResult.entranceCandidates, random)
    val routeResults = calculateRoutes(subResults, entranceResults, contained, random)
    val wallResults = calculateBorderWalls(shapeResult.border, entranceResults) ++ calculateFilledWalls(contained, routeResults)
    val contentResult = CalculateContent(
      locations = contained,
      source = content,
      target = FixedContent(walls = wallResults),
      entrances = entranceResults,
      terrain = terrain,
      random = random
    )

    Result(
      shape = shapeResult,
      templates = subResults,
      entrances = entranceResults,
      content = contentResult,
      terrain = terrain
    )
  }

  private def calculateSubTemplates(t: Map[TemplateId, Template],
                                    shapeResult: Shape.Result,
                                    random: Random
                                   ): Map[Location, Result] = {

    val subResults = templates flatMap (definition => {
      val (sub, min, max) = definition
      val range = Rng.nextRange(random, min, max)

      range map (_ => sub(t)(t, random))
    })

    CalculateTemplateLocations(
      subResults,
      shapeResult,
      random
    )
  }

  private def calculateEntrances(locations: Set[Location], random: Random): Map[Location, Option[ItemKindId]] = {
    CalculateUtils.randomUniqueElementLocations(locations, entrances, Map(), random)
  }

  private def calculateRoutes(subResults: Map[Location, Result],
                              entranceResults: Map[Location, Option[ItemKindId]],
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
                                   entranceResults: Map[Location, Option[ItemKindId]]
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
