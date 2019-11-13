package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.area.feature.Feature
import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.area.template.ContentSelection._
import io.github.fiifoo.scarl.area.template.ContentSource.TemplateSource
import io.github.fiifoo.scarl.area.template.RandomizedContentSource.{ConduitLocations, Entrances, Routing}
import io.github.fiifoo.scarl.area.template.Template.Result
import io.github.fiifoo.scarl.core.creature.FactionId
import io.github.fiifoo.scarl.core.geometry.{Location, Rotation}
import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.world.{TemplateSourceCatalogueId, WorldAssets}

import scala.util.Random

case class RandomizedTemplate(id: TemplateId,
                              shape: Shape,
                              owner: Option[FactionId] = None,
                              border: Option[WallSelection] = None,
                              fill: Option[WallSelection] = None,
                              terrain: Option[TerrainSelection] = None,
                              routing: Option[Routing] = None,
                              templateCatalogue: Option[TemplateSourceCatalogueId] = None,
                              templates: List[TemplateSource] = List(),
                              entrances: Entrances = Entrances(),
                              conduitLocations: ConduitLocations = ConduitLocations(),
                              features: List[Feature] = List(),
                             ) extends Template with RandomizedContentSource {

  def apply(assets: WorldAssets, area: Area, random: Random): Result = {
    val shapeResult = shape(random)
    val subResults = calculateSubTemplates(assets, area, shapeResult, random)

    CalculateRandomizedContent(this, shapeResult, subResults)(assets, area, random)
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
}
