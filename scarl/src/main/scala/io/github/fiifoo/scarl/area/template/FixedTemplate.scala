package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.area.feature.Feature
import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.area.template.Template.{Category, Result}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.world.WorldAssets

import scala.util.Random

case class FixedTemplate(id: TemplateId,
                         shape: Shape,
                         category: Option[Category] = None,
                         power: Option[Int] = None,
                         templates: Map[Location, TemplateId] = Map(),
                         entrances: Map[Location, ItemKindId] = Map(),
                         features: List[Feature] = List(),
                         content: FixedContent = FixedContent(),
                        ) extends Template {

  def apply(assets: WorldAssets, area: Area, random: Random): Result = {
    val shapeResult = shape(random)
    val subResults = templates mapValues (_ (assets.templates)(assets, area, random))
    val contained = CalculateUtils.templateContainedLocations(shapeResult, subResults)
    val contentResult = CalculateContent(
      assets = assets,
      area = area,
      locations = contained,
      target = content,
      entrances = entrances,
      conduits = (0, 0),
      features = features,
      terrain = None,
      random = random
    )

    Result(
      shape = shapeResult,
      templates = subResults,
      entrances = entrances,
      content = contentResult
    )
  }
}
