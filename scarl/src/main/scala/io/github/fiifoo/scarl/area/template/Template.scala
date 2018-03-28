package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.area.template.Template.{Category, Result}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.world.WorldAssets

import scala.util.Random

trait Template {
  val id: TemplateId
  val shape: Shape
  val category: Option[Category]
  val power: Option[Int]

  def apply(assets: WorldAssets,
            area: Area,
            random: Random,
           ): Result
}

object Template {

  trait Category

  case object ChallengeCategory extends Category

  case object TrapCategory extends Category

  val categories: Set[Category] = Set(
    ChallengeCategory,
    TrapCategory,
  )

  case class Result(shape: Shape.Result,
                    templates: Map[Location, Result] = Map(),
                    entrances: Map[Location, ItemKindId] = Map(),
                    content: FixedContent = FixedContent()
                   )

}
