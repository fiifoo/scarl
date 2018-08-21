package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.core.item.Equipment
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.core.math.Rng.{WeightedChoice, WeightedChoices}
import io.github.fiifoo.scarl.world.WorldAssets

import scala.util.Random

sealed trait ContentSelection[T] {
  def apply(assets: WorldAssets, area: Area, random: Random): Option[T]
}

case object ContentSelection {

  sealed trait CreatureSelection extends ContentSelection[CreatureKindId]

  sealed trait ItemSelection extends ContentSelection[ItemKindId]

  sealed trait TemplateSelection extends ContentSelection[TemplateId]

  sealed trait WidgetSelection extends ContentSelection[WidgetKindId]

  case class FixedCreature(kind: CreatureKindId) extends CreatureSelection {
    def apply(assets: WorldAssets, area: Area, random: Random): Option[CreatureKindId] = Some(this.kind)
  }

  case class FixedItem(kind: ItemKindId) extends ItemSelection {
    def apply(assets: WorldAssets, area: Area, random: Random): Option[ItemKindId] = Some(this.kind)
  }

  case class FixedTemplate(template: TemplateId) extends TemplateSelection {
    def apply(assets: WorldAssets, area: Area, random: Random): Option[TemplateId] = Some(this.template)
  }

  case class FixedWidget(kind: WidgetKindId) extends WidgetSelection {
    def apply(assets: WorldAssets, area: Area, random: Random): Option[WidgetKindId] = Some(this.kind)
  }

  case class ThemeCreature(power: Set[CombatPower.Category] = Set()) extends CreatureSelection {
    def apply(assets: WorldAssets, area: Area, random: Random): Option[CreatureKindId] = {
      val choices = assets.themes(area.theme).creatures
      val constraints = if (this.power.isEmpty) CombatPower.categories else this.power

      def getPower(choice: CreatureKindId): Option[Int] = {
        assets.combatPower.average.get(choice)
      }

      selectContent(area, choices, constraints, random, getPower)
    }
  }

  case class ThemeItem(category: Set[ItemKind.Category] = Set(),
                       power: Set[CombatPower.Category] = Set()
                      ) extends ItemSelection {
    def apply(assets: WorldAssets, area: Area, random: Random): Option[ItemKindId] = {
      val choices = assets.themes(area.theme).items
      val categories = if (this.category.isEmpty) ItemKind.categories else this.category
      val constraints = if (this.power.isEmpty) CombatPower.categories else this.power

      def getPower(category: ItemKind.Category, choice: ItemKindId): Option[Int] = {
        assets.combatPower.item.get(category) flatMap (_.get(choice))
      }

      selectCategoryContent(area, choices, categories, constraints, random, getPower)
    }
  }

  case class ThemeEquipment(category: Set[Equipment.Category] = Set(),
                            power: Set[CombatPower.Category] = Set()
                           ) extends ItemSelection {
    def apply(assets: WorldAssets, area: Area, random: Random): Option[ItemKindId] = {
      val choices = assets.themes(area.theme).items
      val categories = if (this.category.isEmpty) Equipment.categories else this.category
      val constraints = if (this.power.isEmpty) CombatPower.categories else this.power

      def getPower(category: Equipment.Category, choice: ItemKindId): Option[Int] = {
        assets.combatPower.equipment.get(category) flatMap (_.get(choice))
      }

      selectCategoryContent(area, choices, categories, constraints, random, getPower)
    }
  }

  case class ThemeTemplate(category: Set[Template.Category] = Set(),
                           power: Set[CombatPower.Category] = Set()
                          ) extends TemplateSelection {
    def apply(assets: WorldAssets, area: Area, random: Random): Option[TemplateId] = {
      val choices = assets.themes(area.theme).templates
      val categories = if (this.category.isEmpty) Template.categories else this.category
      val constraints = if (this.power.isEmpty) CombatPower.categories else this.power

      def getPower(category: Template.Category, choice: TemplateId): Option[Int] = {
        assets.combatPower.template.get(category) flatMap (_.get(choice))
      }

      selectCategoryContent(area, choices, categories, constraints, random, getPower)
    }
  }

  case class ThemeWidget(category: Set[WidgetKind.Category] = Set(),
                         power: Set[CombatPower.Category] = Set()
                        ) extends WidgetSelection {
    def apply(assets: WorldAssets, area: Area, random: Random): Option[WidgetKindId] = {
      val choices = assets.themes(area.theme).widgets
      val categories = if (this.category.isEmpty) WidgetKind.categories else this.category
      val constraints = if (this.power.isEmpty) CombatPower.categories else this.power

      def getPower(category: WidgetKind.Category, choice: WidgetKindId): Option[Int] = {
        assets.combatPower.widget.get(category) flatMap (_.get(choice))
      }

      selectCategoryContent(area, choices, categories, constraints, random, getPower)
    }
  }

  private def selectCategoryContent[T, U](area: Area,
                                          choices: Iterable[WeightedChoice[T]],
                                          categories: Set[U],
                                          constraints: Set[CombatPower.Category],
                                          random: Random,
                                          getPower: (U, T) => Option[Int]
                                         ): Option[T] = {
    def select(categories: Set[U]): Option[T] = {
      if (categories.isEmpty) {
        None
      } else {
        val category = Rng.nextChoice(random, categories)
        val result = selectContent(area, choices, constraints, random, (choice: T) => getPower(category, choice))

        result orElse select(categories - category)
      }
    }

    select(categories)
  }

  private def selectContent[T](area: Area,
                               choices: Iterable[WeightedChoice[T]],
                               constraints: Set[CombatPower.Category],
                               random: Random,
                               getPower: T => Option[Int]
                              ): Option[T] = {
    def select(constraints: Set[CombatPower.Category]): Option[T] = {
      if (constraints.isEmpty) {
        None
      } else {
        val constraint = Rng.nextChoice(random, constraints)
        val power = area.power.get(constraint) orElse Some(CombatPower.all)

        val matching = power map (x => {
          val (min, max) = x

          choices collect {
            case choice if getPower(choice.value).forall(p => p >= min && p <= max) =>
              if (getPower(choice.value).isDefined) {
                choice
              } else {
                // choices without power are applicable to all constraints
                // weight is normalized to reflect this
                choice.copy(weight = choice.weight / constraints.size)
              }
          }
        }) getOrElse List()

        if (matching.isEmpty) {
          select(constraints - constraint)
        } else {
          Some(Rng.nextChoice(random, WeightedChoices(matching)))
        }
      }
    }

    select(constraints)
  }
}
