package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.area.template.Template.{FixedContent, RandomizedContent, Result}
import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.kind._

import scala.util.Random

case class FixedTemplate(id: TemplateId,
                         shape: Shape,
                         templates: Map[Location, TemplateId] = Map(),
                         entrances: Map[Location, Option[ItemKindId]] = Map(),
                         fixedContent: FixedContent = FixedContent(),
                         randomizedContent: RandomizedContent = RandomizedContent(),
                         terrain: Option[TerrainKindId] = None
                        ) extends Template {

  def apply(t: Map[TemplateId, Template], random: Random): Result = {
    val shapeResult = shape(random)
    val subResults = templates mapValues (_ (t)(t, random))
    val contained = CalculateUtils.templateContainedLocations(shapeResult, subResults)
    val contentResult = CalculateContent(
      locations = contained,
      source = randomizedContent,
      target = fixedContent,
      entrances = entrances,
      terrain = terrain,
      random = random
    )

    Result(
      shape = shapeResult,
      templates = subResults,
      entrances = entrances,
      content = contentResult,
      terrain = terrain
    )
  }
}
