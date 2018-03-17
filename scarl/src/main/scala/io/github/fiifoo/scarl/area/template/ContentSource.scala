package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.area.template.ContentSelection._
import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.core.item.Equipment
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.math.{Distribution, Rng}
import io.github.fiifoo.scarl.world.WorldAssets

import scala.util.Random

sealed trait ContentSource[T] {
  val distribution: Distribution

  def apply(assets: WorldAssets, area: Area, random: Random): T
}

object ContentSource {

  case class CreatureSource(selection: CreatureSelection,
                            distribution: Distribution
                           ) extends ContentSource[CreatureKindId] {
    def apply(assets: WorldAssets, area: Area, random: Random): CreatureKindId = {
      selection match {
        case selection: ThemeCreature => select(assets, area, selection, random)
        case selection: FixedCreature => selection.kind
      }
    }

    private def select(assets: WorldAssets,
                       area: Area,
                       selection: ThemeCreature,
                       random: Random
                      ): CreatureKindId = {
      val choices = assets.themes(area.theme).creatures
      val constraints = if (selection.power.isEmpty) CombatPower.categories else selection.power

      def getPower(choice: CreatureKindId): Option[Int] = {
        assets.combatPower.average.get(choice)
      }

      selectContent(area, choices, constraints, random, getPower)
    }
  }

  case class ItemSource(selection: ItemSelection,
                        distribution: Distribution
                       ) extends ContentSource[ItemKindId] {
    def apply(assets: WorldAssets, area: Area, random: Random): ItemKindId = {
      selection match {
        case selection: ThemeEquipment => selectEquipment(assets, area, selection, random)
        case selection: ThemeItem => selectItem(assets, area, selection, random)
        case selection: FixedItem => selection.kind
      }
    }

    private def selectItem(assets: WorldAssets,
                           area: Area,
                           selection: ThemeItem,
                           random: Random
                          ): ItemKindId = {
      val choices = assets.themes(area.theme).items
      val categories = if (selection.category.isEmpty) ItemKind.categories else selection.category
      val constraints = if (selection.power.isEmpty) CombatPower.categories else selection.power

      def getPower(category: ItemKind.Category, choice: ItemKindId): Option[Int] = {
        assets.combatPower.item.get(category) flatMap (_.get(choice))
      }

      selectCategoryContent(area, choices, categories, constraints, random, getPower)
    }

    private def selectEquipment(assets: WorldAssets,
                                area: Area,
                                selection: ThemeEquipment,
                                random: Random
                               ): ItemKindId = {
      val choices = assets.themes(area.theme).items
      val categories = if (selection.category.isEmpty) Equipment.categories else selection.category
      val constraints = if (selection.power.isEmpty) CombatPower.categories else selection.power

      def getPower(category: Equipment.Category, choice: ItemKindId): Option[Int] = {
        assets.combatPower.equipment.get(category) flatMap (_.get(choice))
      }

      selectCategoryContent(area, choices, categories, constraints, random, getPower)
    }
  }

  case class WidgetSource(selection: WidgetSelection,
                          distribution: Distribution
                         ) extends ContentSource[WidgetKindId] {
    def apply(assets: WorldAssets, area: Area, random: Random): WidgetKindId = {
      selection match {
        case selection: ThemeWidget => select(assets, area, selection, random)
        case selection: FixedWidget => selection.kind
      }
    }

    private def select(assets: WorldAssets,
                       area: Area,
                       selection: ThemeWidget,
                       random: Random
                      ): WidgetKindId = {
      val choices = assets.themes(area.theme).widgets
      val categories = if (selection.category.isEmpty) WidgetKind.categories else selection.category
      val constraints = if (selection.power.isEmpty) CombatPower.categories else selection.power

      def getPower(category: WidgetKind.Category, choice: WidgetKindId): Option[Int] = {
        assets.combatPower.widget.get(category) flatMap (_.get(choice))
      }

      selectCategoryContent(area, choices, categories, constraints, random, getPower)
    }
  }

  private def selectCategoryContent[T, U](area: Area,
                                          choices: Set[T],
                                          categories: Set[U],
                                          constraints: Set[CombatPower.Category],
                                          random: Random,
                                          getPower: (U, T) => Option[Int]
                                         ): T = {
    val category = Rng.nextChoice(random, categories)

    selectContent(area, choices, constraints, random, (choice: T) => getPower(category, choice))
  }

  private def selectContent[T](area: Area,
                               choices: Set[T],
                               constraints: Set[CombatPower.Category],
                               random: Random,
                               getPower: T => Option[Int]
                              ): T = {
    val constraint = Rng.nextChoice(random, constraints)

    val matching = area.power.get(constraint) map (x => {
      val (min, max) = x

      choices filter (choice => {
        val power = getPower(choice)

        power.exists(p => p >= min && p <= max)
      })
    }) getOrElse Set()

    if (matching.isEmpty) {
      throw new CalculateFailedException
    }

    Rng.nextChoice(random, matching)
  }
}
