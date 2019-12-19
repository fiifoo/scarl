package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.area.template.FixedContent.MachinerySource
import io.github.fiifoo.scarl.area.template.Template.{Context, Result}
import io.github.fiifoo.scarl.area.theme.ThemeId
import io.github.fiifoo.scarl.core.Tag
import io.github.fiifoo.scarl.core.creature.FactionId
import io.github.fiifoo.scarl.core.geometry.{Location, Rotation}
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.world.WorldAssets

import scala.util.Random

trait Template {
  val id: TemplateId
  val theme: Option[ThemeId]
  val owner: Option[FactionId]

  def apply(assets: WorldAssets,
            context: Context,
            random: Random,
           ): Result
}

object Template {

  trait Category

  case object ChallengeCategory extends Category

  case object RoomCategory extends Category

  case object SpaceCategory extends Category

  case object StorageCategory extends Category

  case object TrapCategory extends Category

  val categories: Set[Category] = Set(
    ChallengeCategory,
    RoomCategory,
    SpaceCategory,
    TrapCategory,
  )

  object Context {
    def apply(area: Area): Context = {
      Context(area.theme, area.owner)
    }
  }

  case class Context(theme: ThemeId,
                     owner: Option[FactionId] = None,
                    ) {
    def apply(template: Template): Context = {
      this.copy(
        theme = template.theme getOrElse this.theme,
        owner = template.owner orElse this.owner,
      )
    }
  }

  case class Result(shape: Shape.Result,
                    owner: Option[FactionId] = None,
                    templates: Map[Location, Result] = Map(),
                    entrances: Set[Location] = Set(),
                    content: ResultContent = ResultContent()
                   ) {

    def rotate(rotation: Rotation): Result = Result(
      owner = this.owner,
      shape = this.shape.rotate(rotation),
      templates = this.rotateTemplates(rotation),
      entrances = this.entrances map rotation.apply,
      content = this.content.rotate(rotation)
    )

    private def rotateTemplates(rotation: Rotation): Map[Location, Result] = {
      this.templates map (x => {
        val (location, result) = x

        rotation(location) -> result.rotate(Rotation(rotation.value, 0, 0))
      })
    }
  }

  case class ResultContent(conduitLocations: Map[Location, Option[Tag]] = Map(),
                           creatures: Map[Location, (CreatureKindId, Set[Tag])] = Map(),
                           gatewayLocations: Set[Location] = Set(),
                           items: Map[Location, List[(ItemKindId, Set[Tag])]] = Map(),
                           machinery: Set[MachinerySource] = Set(),
                           restrictedLocations: Set[Location] = Set(),
                           terrains: Map[Location, (TerrainKindId, Set[Tag])] = Map(),
                           walls: Map[Location, (WallKindId, Set[Tag])] = Map(),
                           widgets: Map[Location, (WidgetKindId, Set[Tag])] = Map()
                          ) {

    def rotate(rotation: Rotation): ResultContent = ResultContent(
      conduitLocations = rotation.mapKey(this.conduitLocations),
      creatures = rotation.mapKey(this.creatures),
      gatewayLocations = this.gatewayLocations map rotation.apply,
      items = rotation.mapKey(this.items),
      machinery = rotateMachinery(rotation),
      restrictedLocations = this.restrictedLocations map rotation.apply,
      terrains = rotation.mapKey(this.terrains),
      walls = rotation.mapKey(this.walls),
      widgets = rotation.mapKey(this.widgets),
    )

    private def rotateMachinery(rotation: Rotation): Set[MachinerySource] = {
      this.machinery map (machinery => {
        machinery.copy(
          controls = machinery.controls map rotation.apply,
          targets = machinery.targets map rotation.apply
        )
      })
    }
  }

}
