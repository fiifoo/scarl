package io.github.fiifoo.scarl.area.feature

import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.area.template.{CalculateFailedException, ContentSource, FixedContent}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.world.WorldAssets

import scala.util.Random

object Utils {

  def freeLocations(content: FixedContent, locations: Set[Location]): Set[Location] = {
    locations -- content.gatewayLocations -- content.conduitLocations.keys -- content.walls.keys
  }

  def randomUniqueElementLocations[T](assets: WorldAssets,
                                      area: Area,
                                      locations: Set[Location],
                                      sources: List[ContentSource[T]],
                                      existing: Map[Location, T],
                                      random: Random,
                                     ): Map[Location, T] = {
    val elements = getElements(assets, area, sources, random)

    val (result, _) = (elements foldLeft(existing, locations -- existing.keys)) ((carry, element) => {
      val (result, choices) = carry
      if (choices.isEmpty) {
        throw new CalculateFailedException
      }
      val location = Rng.nextChoice(random, choices)

      (result + (location -> element), choices - location)
    })

    result
  }

  def randomElementLocations[T](assets: WorldAssets,
                                area: Area,
                                locations: Set[Location],
                                sources: List[ContentSource[T]],
                                existing: Map[Location, List[T]],
                                random: Random,
                               ): Map[Location, List[T]] = {
    if (locations.isEmpty) {
      throw new CalculateFailedException
    }

    val elements = getElements(assets, area, sources, random)

    (elements foldLeft existing) ((result, element) => {
      val location = Rng.nextChoice(random, locations)

      if (result.isDefinedAt(location)) {
        result + (location -> (element :: result(location)))
      } else {
        result + (location -> List(element))
      }
    })
  }

  private def getElements[T](assets: WorldAssets, area: Area, sources: List[ContentSource[T]], random: Random): List[T] = {
    sources flatMap (source => {
      val distribution = source.distribution
      val range = Rng.nextRange(random, distribution)

      range map (_ => source(assets, area, random))
    })
  }
}
