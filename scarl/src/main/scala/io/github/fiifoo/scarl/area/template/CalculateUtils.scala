package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.core.{Location, Rng}

import scala.util.Random

object CalculateUtils {

  def templateContainedLocations(shape: Shape.Result, subs: Map[Location, Template.Result]) = {
    subs.foldLeft(shape.contained)((contained, data) => {
      val (location, sub) = data

      contained -- sub.shape.border.map(location.add) -- sub.shape.contained.map(location.add)
    })
  }

  def randomElementLocations[T](locations: Set[Location],
                                source: List[(T, Int, Int)],
                                existing: Map[Location, List[T]],
                                random: Random
                               ): Map[Location, List[T]] = {
    if (source.isEmpty) {
      return Map()
    }
    if (locations.isEmpty) {
      throw new CalculateFailedException
    }

    val elements = source flatMap (definition => {
      val (element, min, max) = definition
      val range = Rng.nextRange(random, min, max)

      range map (_ => element)
    })

    val fold = elements.foldLeft[(Map[Location, List[T]], Set[Location])](existing, locations) _

    val (results, _) = fold((carry, element) => {
      val (results, choices) = carry
      val location = Rng.nextChoice(random, choices)

      if (results.isDefinedAt(location)) {
        (results + (location -> (element :: results(location))), choices)
      } else {
        (results + (location -> List(element)), choices)
      }
    })

    results
  }

  def randomUniqueElementLocations[T](locations: Set[Location],
                                      source: List[(T, Int, Int)],
                                      existing: Map[Location, T],
                                      random: Random
                                     ): Map[Location, T] = {

    val elements = source flatMap (definition => {
      val (element, min, max) = definition
      val range = Rng.nextRange(random, min, max)

      range map (_ => element)
    })

    val fold = elements.foldLeft[(Map[Location, T], Set[Location])](Map(), locations -- existing.keys) _

    val (results, _) = fold((carry, element) => {
      val (results, choices) = carry
      if (choices.isEmpty) {
        throw new CalculateFailedException
      }

      val location = Rng.nextChoice(random, choices)

      (results + (location -> element), choices - location)
    })

    results
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
