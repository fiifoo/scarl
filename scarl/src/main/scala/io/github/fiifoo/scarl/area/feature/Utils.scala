package io.github.fiifoo.scarl.area.feature

import io.github.fiifoo.scarl.area.template.ContentSelection.{FixedTerrain, ThemeTerrain}
import io.github.fiifoo.scarl.area.template.{CalculateFailedException, ContentSelection, ContentSource, FixedContent}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind.{TerrainKind, TerrainKindId}
import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.world.WorldAssets

import scala.util.Random

object Utils {

  def freeLocations(assets: WorldAssets, content: FixedContent, locations: Set[Location]): Set[Location] = {
    locations --
      content.restrictedLocations --
      content.gatewayLocations --
      content.conduitLocations.keys --
      content.walls.keys --
      (content.terrains collect {
        case (
          location: Location,
          selection: ContentSelection.TerrainSelection
          ) if isImpassableTerrainSelection(assets)(selection) => location
      })
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

  private def isImpassableTerrainSelection(assets: WorldAssets)
                                          (selection: ContentSelection.TerrainSelection): Boolean = {
    def isImpassableTerrain(terrain: TerrainKindId): Boolean = assets.kinds.terrains(terrain).impassable

    selection match {
      case selection: FixedTerrain if isImpassableTerrain(selection.kind) => true
      case selection: ThemeTerrain if selection.category.contains(TerrainKind.ImpassableCategory) => true
      case _ => false
    }
  }
}
