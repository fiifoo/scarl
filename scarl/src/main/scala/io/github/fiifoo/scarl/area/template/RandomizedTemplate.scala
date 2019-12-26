package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.feature.Feature
import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.area.template.ContentSelection._
import io.github.fiifoo.scarl.area.template.ContentSource.TemplateSource
import io.github.fiifoo.scarl.area.template.RandomizedContentSource.{ConduitLocations, Entrances, Routing}
import io.github.fiifoo.scarl.area.template.Template.{Context, Result}
import io.github.fiifoo.scarl.area.theme.ThemeId
import io.github.fiifoo.scarl.core.creature.FactionId
import io.github.fiifoo.scarl.core.geometry.{Location, Rotation}
import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.world.{TemplateSourceCatalogueId, WorldAssets}

import scala.util.Random

case class RandomizedTemplate(id: TemplateId,
                              shape: Shape,
                              unique: Boolean = false,
                              theme: Option[ThemeId] = None,
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

  def apply(assets: WorldAssets, context: Context, random: Random): Result = {
    val shapeResult = shape(random)
    val subResults = calculateSubTemplates(assets, context, shapeResult, random)

    CalculateRandomizedContent(this, shapeResult, subResults)(assets, context(this), random)
  }

  private def calculateSubTemplates(assets: WorldAssets,
                                    context: Context,
                                    shape: Shape.Result,
                                    random: Random
                                   ): Map[Location, Result] = {
    val sources = this.templates ::: this.templateCatalogue
      .flatMap(assets.catalogues.templateSources.get)
      .map(_.apply(assets.catalogues.templateSources))
      .getOrElse(Nil)

    val selections = sources flatMap (source => {
      val distribution = source.distribution
      val range = Rng.nextRange(random, distribution)

      range map (i => (source.selection, i < source.required))
    })

    val initial: (List[(Result, Boolean)], Context) = (Nil, context(this))

    val (results, _) = (selections foldLeft initial) ((carry, item) => {
      val (results, context) = carry
      val (selection, required) = item

      val result = selection(assets, context, random) map (template => {
        val result = CalculateTemplate(assets, context, random)(template(assets.templates))
        val rotation = Rotation(random, result.shape.outerWidth, result.shape.outerHeight).reverse

        result.rotate(rotation)
      }) match {
        case Some(x) => x
        case None => throw new CalculateFailedException
      }

      ((result, required) :: results, context(assets, result))
    })

    CalculateTemplateLocations(
      results,
      shape,
      random
    )
  }
}
