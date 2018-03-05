package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.area.template.Template.Result
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.world.WorldAssets

import scala.util.Random

trait Template {
  val id: TemplateId

  def apply(assets: WorldAssets,
            area: Area,
            random: Random,
           ): Result
}

object Template {

  case class Result(shape: Shape.Result,
                    templates: Map[Location, Result] = Map(),
                    entrances: Map[Location, ItemKindId] = Map(),
                    content: FixedContent = FixedContent()
                   )

}
