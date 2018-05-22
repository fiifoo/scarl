package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.area.template.Template.{Category, Result}
import io.github.fiifoo.scarl.core.geometry.{Location, Rotation}
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
                   ) {

    def rotate(rotation: Rotation): Result = Result(
      shape = this.shape.rotate(rotation),
      templates = this.rotateTemplates(rotation),
      entrances = rotation.mapKey(this.entrances),
      content = this.content.rotate(rotation)
    )

    private def rotateTemplates(rotation: Rotation): Map[Location, Result] = {
      this.templates map (x => {
        val (location, result) = x

        rotation(location) -> result.rotate(rotation)
      })
    }
  }

}
