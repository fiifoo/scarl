package io.github.fiifoo.scarl.area.feature

import io.github.fiifoo.scarl.area.feature.RandomizedContentFeature.{CreatureSource, ItemSource, WidgetSource}
import io.github.fiifoo.scarl.area.feature.Utils.freeLocations
import io.github.fiifoo.scarl.area.template.{CalculateFailedException, FixedContent}
import io.github.fiifoo.scarl.area.theme.ContentSelection.{FixedCreature, FixedItem, ThemeCreature, ThemeItem}
import io.github.fiifoo.scarl.area.theme.{CreatureSelection, ItemSelection, Theme}
import io.github.fiifoo.scarl.core.kind.{CreatureKindId, ItemKindId, WidgetKindId}
import io.github.fiifoo.scarl.core.{Distribution, Location, Rng}
import io.github.fiifoo.scarl.world.WorldAssets

import scala.util.Random

case class RandomizedContentFeature(creatures: List[CreatureSource] = List(),
                                    items: List[ItemSource] = List(),
                                    widgets: List[WidgetSource] = List()
                                   ) extends Feature {

  def apply(assets: WorldAssets,
            theme: Theme,
            content: FixedContent,
            locations: Set[Location],
            entrances: Set[Location],
            random: Random
           ): FixedContent = {
    val free = freeLocations(content, locations)

    val creatures = RandomizedContentFeature.randomUniqueElementLocations(
      assets = assets,
      theme = theme,
      locations = free,
      sources = this.creatures,
      existing = content.creatures,
      random = random,
    )

    val widgets = RandomizedContentFeature.randomUniqueElementLocations(
      assets = assets,
      theme = theme,
      locations = free,
      sources = this.widgets,
      existing = content.widgets,
      random = random,
    )
    val items = RandomizedContentFeature.randomElementLocations(
      assets = assets,
      theme = theme,
      locations = free -- widgets.keys,
      sources = this.items,
      existing = content.items,
      random = random,
    )

    content.copy(
      creatures = creatures,
      items = items,
      widgets = widgets
    )
  }
}

object RandomizedContentFeature {

  sealed trait ContentSource[T] {
    val distribution: Distribution

    def getSelection(assets: WorldAssets, theme: Theme, random: Random): T
  }

  case class CreatureSource(selection: CreatureSelection,
                            distribution: Distribution
                           ) extends ContentSource[CreatureKindId] {
    def getSelection(assets: WorldAssets, theme: Theme, random: Random): CreatureKindId = {
      selection match {
        case selection: ThemeCreature => getThemeSelection(assets, theme, selection, random)
        case selection: FixedCreature => selection.kind
      }
    }

    private def getThemeSelection(assets: WorldAssets, theme: Theme, selection: ThemeCreature, random: Random): CreatureKindId = {
      val choices = theme.creatures filter (creature => {
        val power = assets.combatPower.average.get(creature)

        power.exists(power => {
          power >= selection.minCombatPower && power <= selection.maxCombatPower
        })
      })
      if (choices.isEmpty) {
        throw new CalculateFailedException
      }

      Rng.nextChoice(random, choices)
    }
  }

  case class ItemSource(selection: ItemSelection,
                        distribution: Distribution
                       ) extends ContentSource[ItemKindId] {
    def getSelection(assets: WorldAssets, theme: Theme, random: Random): ItemKindId = {
      selection match {
        case ThemeItem => Rng.nextChoice(random, theme.items)
        case selection: FixedItem => selection.kind
      }
    }
  }

  case class WidgetSource(selection: WidgetKindId, distribution: Distribution) extends ContentSource[WidgetKindId] {
    def getSelection(assets: WorldAssets, theme: Theme, random: Random): WidgetKindId = selection
  }

  def randomUniqueElementLocations[T](assets: WorldAssets,
                                      theme: Theme,
                                      locations: Set[Location],
                                      sources: List[ContentSource[T]],
                                      existing: Map[Location, T],
                                      random: Random,
                                     ): Map[Location, T] = {
    val elements = getElements(assets, theme, sources, random)

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
                                theme: Theme,
                                locations: Set[Location],
                                sources: List[ContentSource[T]],
                                existing: Map[Location, List[T]],
                                random: Random,
                               ): Map[Location, List[T]] = {
    if (locations.isEmpty) {
      throw new CalculateFailedException
    }

    val elements = getElements(assets, theme, sources, random)

    (elements foldLeft existing) ((result, element) => {
      val location = Rng.nextChoice(random, locations)

      if (result.isDefinedAt(location)) {
        result + (location -> (element :: result(location)))
      } else {
        result + (location -> List(element))
      }
    })
  }

  private def getElements[T](assets: WorldAssets, theme: Theme, sources: List[ContentSource[T]], random: Random): List[T] = {
    sources flatMap (source => {
      val distribution = source.distribution
      val range = Rng.nextRange(random, distribution)

      range map (_ => source.getSelection(assets, theme, random))
    })
  }

}