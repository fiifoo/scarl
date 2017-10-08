package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.feature.Feature
import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.area.template.Template.Result
import io.github.fiifoo.scarl.area.theme.Theme
import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.world.WorldAssets

import scala.util.Random

case class FixedTemplate(id: TemplateId,
                         shape: Shape,
                         templates: Map[Location, TemplateId] = Map(),
                         entrances: Map[Location, ItemKindId] = Map(),
                         features: List[Feature] = List(),
                         content: FixedContent = FixedContent(),
                        ) extends Template {

  def apply(assets: WorldAssets, theme: Theme, random: Random): Result = {
    val shapeResult = shape(random)
    val subResults = templates mapValues (_ (assets.templates)(assets, theme, random))
    val contained = CalculateUtils.templateContainedLocations(shapeResult, subResults)
    val contentResult = CalculateContent(
      assets = assets,
      theme = theme,
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
      content = contentResult,
    )
  }
}
