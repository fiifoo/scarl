package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.math.Rng

import scala.util.Random

object CalculateUtils {

  def templateContainedLocations(shape: Shape.Result, subs: Map[Location, Template.Result]) = {
    subs.foldLeft(shape.contained)((contained, data) => {
      val (location, sub) = data

      contained -- sub.shape.border.map(location.add) -- sub.shape.contained.map(location.add)
    })
  }

  def randomUniqueLocations(locations: Set[Location],
                            count: (Int, Int),
                            existing: Set[Location],
                            random: Random
                           ): Set[Location] = {

    val (min, max) = count
    val range = Rng.nextRange(random, min, max)
    val fold = range.foldLeft[(Set[Location], Set[Location])](Set(), locations -- existing) _

    val (results, _) = fold((carry, _) => {
      val (results, choices) = carry
      if (choices.isEmpty) {
        throw new CalculateFailedException
      }

      val location = Rng.nextChoice(random, choices)

      (results + location, choices - location)
    })

    results
  }
}
