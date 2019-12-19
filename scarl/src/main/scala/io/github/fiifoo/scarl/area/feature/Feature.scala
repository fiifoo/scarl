package io.github.fiifoo.scarl.area.feature

import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.area.template.FixedContent
import io.github.fiifoo.scarl.area.template.Template.Context
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.world.WorldAssets

import scala.util.Random

trait Feature {
  def apply(assets: WorldAssets,
            context: Context,
            shape: Shape.Result,
            content: FixedContent,
            locations: Set[Location],
            entrances: Set[Location],
            subEntrances: Set[Location],
            random: Random
           ): FixedContent
}
