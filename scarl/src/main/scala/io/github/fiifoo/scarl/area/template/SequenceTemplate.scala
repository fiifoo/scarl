package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.feature.Feature
import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.area.template.ContentSelection._
import io.github.fiifoo.scarl.area.template.RandomizedContentSource.{ConduitLocations, Entrances, Routing}
import io.github.fiifoo.scarl.area.template.Template.{Context, Result}
import io.github.fiifoo.scarl.area.theme.ThemeId
import io.github.fiifoo.scarl.core.creature.FactionId
import io.github.fiifoo.scarl.core.geometry.{Location, Rotation}
import io.github.fiifoo.scarl.world.WorldAssets

import scala.util.Random

case class SequenceTemplate(id: TemplateId,
                            theme: Option[ThemeId] = None,
                            owner: Option[FactionId] = None,
                            border: Option[WallSelection] = None,
                            fill: Option[WallSelection] = None,
                            terrain: Option[TerrainSelection] = None,
                            routing: Option[Routing] = None,
                            templates: List[TemplateSelection] = List(),
                            entrances: Entrances = Entrances(),
                            conduitLocations: ConduitLocations = ConduitLocations(),
                            features: List[Feature] = List(),
                           ) extends Template with RandomizedContentSource {

  def apply(assets: WorldAssets, context: Context, random: Random): Result = {
    val (subResults, shapeResult) = this.calculateSequence(assets, context, random)

    CalculateRandomizedContent(this, shapeResult, subResults)(assets, context(this), random)
  }

  private def calculateSequence(assets: WorldAssets,
                                context: Context,
                                random: Random
                               ): (Map[Location, Result], Shape.Result) = {
    val templates = this.templates map (selection => {
      selection.apply(assets, context(this), random) map (template => {
        val result = CalculateTemplate(assets, context(this), random)(template(assets.templates))
        val rotation = Rotation(random, result.shape.outerWidth, result.shape.outerHeight).reverse

        result.rotate(rotation)
      }) match {
        case Some(x) => x
        case None => throw new CalculateFailedException
      }
    })

    CalculateTemplateSequence(templates)
  }
}
