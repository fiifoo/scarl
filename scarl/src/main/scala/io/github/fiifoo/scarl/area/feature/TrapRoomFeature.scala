package io.github.fiifoo.scarl.area.feature

import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.area.feature.Utils._
import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.area.template.ContentSelection.{ItemSelection, WidgetSelection}
import io.github.fiifoo.scarl.area.template.ContentSource.WidgetSource
import io.github.fiifoo.scarl.area.template.FixedContent.MachinerySource
import io.github.fiifoo.scarl.area.template.{CalculateFailedException, FixedContent}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.mechanism.CreateEntityMechanism
import io.github.fiifoo.scarl.world.WorldAssets

import scala.util.Random

case class TrapRoomFeature(bait: ItemSelection,
                           trigger: WidgetSelection,
                           traps: List[WidgetSource],
                           description: Option[String]
                          ) extends Feature {

  def apply(assets: WorldAssets,
            area: Area,
            shape: Shape.Result,
            content: FixedContent,
            locations: Set[Location],
            entrances: Set[Location],
            subEntrances: Set[Location],
            random: Random
           ): FixedContent = {
    val free = freeLocations(assets, content, locations) -- content.widgets.keys
    if (free.isEmpty) {
      throw new CalculateFailedException
    }
    val location = Rng.nextChoice(random, free)
    val getTrapsMachinery = (this.getTrapsMachinery(assets, area, free - location, location, random) _).tupled

    val items = content.items + (location -> (this.bait :: content.items.getOrElse(location, Nil)))
    val machinery = content.machinery ++ (this.traps.zipWithIndex map getTrapsMachinery).toSet
    val widgets = content.widgets + (location -> this.trigger)

    content.copy(
      items = items,
      machinery = machinery,
      widgets = widgets
    )
  }

  private def getTrapsMachinery(assets: WorldAssets,
                                area: Area,
                                locations: Set[Location],
                                control: Location,
                                random: Random,
                               )
                               (traps: WidgetSource,
                                index: Int
                               ): MachinerySource = {
    val count = traps.distribution.value(random)
    val widget = traps.selection.apply(assets, area, random)
    if (widget.isEmpty || count > locations.size) {
      throw new CalculateFailedException
    }

    val targets = Rng.nextChoices(random, locations, count)
    val mechanism = CreateEntityMechanism(
      kind = widget.get,
      createDescription = None
    )

    MachinerySource(mechanism, Set(control), targets, description, disposable = true)
  }
}
