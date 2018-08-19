package io.github.fiifoo.scarl.area.feature

import io.github.fiifoo.scarl.area.template.{CalculateFailedException, ContentSelection, ContentSource, FixedContent}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.math.Rng

import scala.util.Random

object Utils {

  def freeLocations(content: FixedContent, locations: Set[Location]): Set[Location] = {
    locations --
      content.restrictedLocations --
      content.gatewayLocations --
      content.conduitLocations.keys --
      content.walls.keys
  }

  def randomUniqueSelectionLocations[T](locations: Set[Location],
                                        sources: List[ContentSource[T]],
                                        existing: Map[Location, ContentSelection[T]],
                                        random: Random,
                                       ): Map[Location, ContentSelection[T]] = {
    val selections = getSelections(sources, random)

    val (result, _) = (selections foldLeft(existing, locations -- existing.keys)) ((carry, selection) => {
      val (result, choices) = carry
      if (choices.isEmpty) {
        throw new CalculateFailedException
      }
      val location = Rng.nextChoice(random, choices)

      (result + (location -> selection), choices - location)
    })

    result
  }

  def randomSelectionLocations[T](locations: Set[Location],
                                  sources: List[ContentSource[T]],
                                  existing: Map[Location, List[ContentSelection[T]]],
                                  random: Random,
                                 ): Map[Location, List[ContentSelection[T]]] = {
    if (locations.isEmpty) {
      throw new CalculateFailedException
    }

    val selections = getSelections(sources, random)

    (selections foldLeft existing) ((result, selection) => {
      val location = Rng.nextChoice(random, locations)

      if (result.isDefinedAt(location)) {
        result + (location -> (selection :: result(location)))
      } else {
        result + (location -> List(selection))
      }
    })
  }

  private def getSelections[T](sources: List[ContentSource[T]], random: Random): List[ContentSelection[T]] = {
    sources flatMap (source => {
      val distribution = source.distribution
      val range = Rng.nextRange(random, distribution)

      range map (_ => source.selection)
    })
  }
}
