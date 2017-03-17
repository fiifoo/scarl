package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.area.template.Template.Result
import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.kind._

import scala.util.Random

trait Template {
  val id: TemplateId

  def apply(t: Map[TemplateId, Template], random: Random): Result
}

object Template {

  case class Result(shape: Shape.Result,
                    templates: Map[Location, Result] = Map(),
                    entrances: Map[Location, Option[ItemKindId]] = Map(),
                    content: FixedContent = FixedContent(),
                    terrain: Option[TerrainKindId] = None
                   )

  case class FixedContent(creatures: Map[Location, CreatureKindId] = Map(),
                          items: Map[Location, List[ItemKindId]] = Map(),
                          terrains: Map[Location, TerrainKindId] = Map(),
                          walls: Map[Location, WallKindId] = Map(),
                          widgets: Map[Location, WidgetKindId] = Map()
                         )

  /** List(what, min, max) */
  case class RandomizedContent(creatures: List[(CreatureKindId, Int, Int)] = List(),
                               items: List[(ItemKindId, Int, Int)] = List(),
                               terrains: List[(TerrainKindId, Int, Int)] = List(),
                               widgets: List[(WidgetKindId, Int, Int)] = List()
                              )

}
