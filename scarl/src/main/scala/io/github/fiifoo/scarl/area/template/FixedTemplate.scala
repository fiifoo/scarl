package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.area.feature.Feature
import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.area.template.ContentSelection.{DoorSelection, TerrainSelection}
import io.github.fiifoo.scarl.area.template.Template.Result
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.world.WorldAssets

import scala.util.Random

case class FixedTemplate(id: TemplateId,
                         shape: Shape,
                         power: Option[Int] = None,
                         terrain: Option[TerrainSelection] = None,
                         templates: Map[Location, TemplateId] = Map(),
                         entrances: Map[Location, DoorSelection] = Map(),
                         features: List[Feature] = List(),
                         content: FixedContent = FixedContent(),
                        ) extends Template {

  def apply(assets: WorldAssets, area: Area, random: Random): Result = {
    val shapeResult = shape(random)
    val subResults = templates transform ((_, template) => template(assets.templates)(assets, area, random))
    val subEntrances = (subResults map (x => {
      val (location, subResult) = x

      subResult.entrances map location.add
    })).toSet.flatten

    val contained = CalculateUtils.templateContainedLocations(shapeResult, subResults)
    val contentResult = CalculateContent(
      assets = assets,
      area = area,
      shape = shapeResult,
      target = content,
      locations = contained,
      entrances = entrances,
      subEntrances = subEntrances,
      features = features,
      terrain = terrain,
      random = random
    )

    Result(
      shape = shapeResult,
      templates = subResults,
      entrances = entrances.keys.toSet,
      content = contentResult
    )
  }
}
