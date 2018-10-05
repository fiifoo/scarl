package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.area.feature.{Feature, Utils}
import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.area.template.ContentSelection._
import io.github.fiifoo.scarl.area.template.ContentSource.{ItemSource, TemplateSource}
import io.github.fiifoo.scarl.area.template.RandomizedTemplate.{ConduitLocations, Entrances}
import io.github.fiifoo.scarl.area.template.Template.Result
import io.github.fiifoo.scarl.core.Tag
import io.github.fiifoo.scarl.core.geometry.{Location, Rotation}
import io.github.fiifoo.scarl.core.math.Distribution.Uniform
import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.world.{TemplateSourceCatalogueId, WorldAssets}

import scala.util.Random

object RandomizedTemplate {

  case class Entrances(min: Int = 0,
                       max: Int = 0,
                       door: Option[DoorSelection] = None
                      )

  case class ConduitLocations(min: Int = 0,
                              max: Int = 0,
                              tag: Option[Tag] = None
                             )

}

case class RandomizedTemplate(id: TemplateId,
                              shape: Shape,
                              power: Option[Int] = None,
                              border: Option[WallSelection] = None,
                              fill: Option[WallSelection] = None,
                              terrain: Option[TerrainSelection] = None,
                              templateCatalogue: Option[TemplateSourceCatalogueId] = None,
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

      subResult.entrances map location.add
    })).toSet.flatten

    val contained = CalculateUtils.templateContainedLocations(shapeResult, subResults)
    val entranceResults = calculateEntrances(assets, area, shapeResult.entranceCandidates, random)
    val routeResults = calculateRoutes(entranceResults, subEntrances, contained, random)
    val wallResults = calculateBorderWalls(shapeResult.border, entranceResults) ++
      calculateFilledWalls(contained, routeResults)
    val contentResult = CalculateContent(
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
      entrances = entranceResults.keys.toSet,
      content = contentResult
    )
  }

  private def calculateSubTemplates(assets: WorldAssets,
                                    area: Area,
                                    shape: Shape.Result,
                                    random: Random
                                   ): Map[Location, Result] = {
    def calculate(source: TemplateSource): Iterable[(Result, Boolean)] = {
      val result = source.selection.apply(assets, area, random) map (sub => {
        val range = Rng.nextRange(random, source.distribution)

        range map (i => {
          val required = i < source.required
          val result = sub(assets.templates)(assets, area, random)
          val rotation = Rotation(random, result.shape.outerWidth, result.shape.outerHeight).reverse

          (result.rotate(rotation), required)
        })
      })

      if (result.isEmpty) {
        throw new CalculateFailedException
      }

      result.get
    }

    val templates = this.templates ::: this.templateCatalogue
      .flatMap(assets.catalogues.templateSources.get)
      .map(_.apply(assets.catalogues.templateSources))
      .getOrElse(Nil)

    CalculateTemplateLocations(
      templates flatMap calculate,
      shape,
      random
    )
  }

  private def calculateEntrances(assets: WorldAssets,
                                 area: Area, locations: Set[Location],
                                 random: Random
                                ): Map[Location, DoorSelection] = {
    val selection = this.entrances.door.getOrElse(ThemeDoor())
    val distribution = Uniform(this.entrances.min, this.entrances.max)
    val source = ItemSource(selection, distribution)

    Utils.randomUniqueSelectionLocations(locations, List(source), Map(), random)
  }

  private def calculateRoutes(entranceResults: Map[Location, _],
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
                                   entranceResults: Map[Location, _]
                                  ): Map[Location, WallSelection] = {
    border map (border => borders
      .filterNot(entranceResults.isDefinedAt)
      .map((_, border))
      .toMap
      ) getOrElse Map()
  }

  private def calculateFilledWalls(contained: Set[Location],
                                   routeResults: Set[Location]
                                  ): Map[Location, WallSelection] = {
    fill map (fill => contained
      .diff(routeResults)
      .map((_, fill))
      .toMap
      ) getOrElse Map()
  }
}
