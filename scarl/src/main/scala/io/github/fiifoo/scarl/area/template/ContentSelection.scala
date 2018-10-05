package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.core.Tag
import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.core.item.Equipment
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.core.math.Rng.{WeightedChoice, WeightedChoices}
import io.github.fiifoo.scarl.world.WorldAssets

import scala.util.Random

sealed trait ContentSelection[T] {
  def apply(assets: WorldAssets, area: Area, random: Random): Option[T]

  val tags: Set[Tag]
}

case object ContentSelection {

  type CreatureSelection = ContentSelection[CreatureKindId]

  type DoorSelection = ContentSelection[ItemKindId]

  type ItemSelection = ContentSelection[ItemKindId]

  type TemplateSelection = ContentSelection[TemplateId]

  type TerrainSelection = ContentSelection[TerrainKindId]

  type WidgetSelection = ContentSelection[WidgetKindId]

  type WallSelection = ContentSelection[WallKindId]

  case class FixedCreature(kind: CreatureKindId, tags: Set[Tag] = Set()) extends CreatureSelection {
    def apply(assets: WorldAssets, area: Area, random: Random): Option[CreatureKindId] = Some(this.kind)
  }

  case class FixedDoor(kind: ItemKindId, tags: Set[Tag] = Set()) extends DoorSelection {
    def apply(assets: WorldAssets, area: Area, random: Random): Option[ItemKindId] = Some(this.kind)
  }

  case class FixedItem(kind: ItemKindId, tags: Set[Tag] = Set()) extends ItemSelection {
    def apply(assets: WorldAssets, area: Area, random: Random): Option[ItemKindId] = Some(this.kind)
  }

  case class FixedTemplate(template: TemplateId) extends TemplateSelection {
    val tags: Set[Tag] = Set()

    def apply(assets: WorldAssets, area: Area, random: Random): Option[TemplateId] = Some(this.template)
  }

  case class FixedTerrain(kind: TerrainKindId, tags: Set[Tag] = Set()) extends TerrainSelection {
    def apply(assets: WorldAssets, area: Area, random: Random): Option[TerrainKindId] = Some(this.kind)
  }

  case class FixedWall(kind: WallKindId, tags: Set[Tag] = Set()) extends WallSelection {
    def apply(assets: WorldAssets, area: Area, random: Random): Option[WallKindId] = Some(this.kind)
  }

  case class FixedWidget(kind: WidgetKindId, tags: Set[Tag] = Set()) extends WidgetSelection {
    def apply(assets: WorldAssets, area: Area, random: Random): Option[WidgetKindId] = Some(this.kind)
  }

  case class ThemeCreature(category: Set[CreatureKind.Category] = Set(),
                           power: Set[CombatPower.Category] = Set(),
                           tags: Set[Tag] = Set()
                          ) extends CreatureSelection {
    def apply(assets: WorldAssets, area: Area, random: Random): Option[CreatureKindId] = {
      val choices = assets.catalogues.creatures(assets.themes(area.theme).creatures)
        .apply(assets.catalogues.creatures)
      val categories: Set[CreatureKind.Category] = if (this.category.isEmpty) Set(CreatureKind.DefaultCategory) else this.category
      val constraints = if (this.power.isEmpty) CombatPower.categories else this.power

      def getPower(category: CreatureKind.Category, choice: CreatureKindId): Option[Int] = {
        assets.combatPower.average.get(choice)
      }

      selectCategoryContentByPower(area, choices, categories, constraints, random, getPower)
    }
  }

  case class ThemeDoor(category: Set[ItemKind.DoorCategory] = Set(),
                       tags: Set[Tag] = Set()
                      ) extends DoorSelection {
    def apply(assets: WorldAssets, area: Area, random: Random): Option[ItemKindId] = {
      val choices = assets.catalogues.items(assets.themes(area.theme).items)
        .apply(assets.catalogues.items)
        .filterKeys(_.isInstanceOf[ItemKind.DoorCategory])
        .map(x => x._1.asInstanceOf[ItemKind.DoorCategory] -> x._2)
      val categories: Set[ItemKind.DoorCategory] = if (this.category.isEmpty) Set(ItemKind.DefaultDoorCategory) else this.category

      selectCategoryContent(area, choices, categories, random)
    }
  }

  case class ThemeItem(category: Set[ItemKind.Category] = Set(),
                       power: Set[CombatPower.Category] = Set(),
                       tags: Set[Tag] = Set()
                      ) extends ItemSelection {
    def apply(assets: WorldAssets, area: Area, random: Random): Option[ItemKindId] = {
      val choices = assets.catalogues.items(assets.themes(area.theme).items)
        .apply(assets.catalogues.items)
      val categories: Set[ItemKind.Category] = if (this.category.isEmpty) Set(ItemKind.UtilityCategory) else this.category
      val constraints = if (this.power.isEmpty) CombatPower.categories else this.power

      def getPower(category: ItemKind.Category, choice: ItemKindId): Option[Int] = {
        assets.kinds.items(choice).power
      }

      selectCategoryContentByPower(area, choices, categories, constraints, random, getPower)
    }
  }

  case class ThemeEquipment(category: Set[Equipment.Category] = Set(),
                            power: Set[CombatPower.Category] = Set(),
                            tags: Set[Tag] = Set()
                           ) extends ItemSelection {
    def apply(assets: WorldAssets, area: Area, random: Random): Option[ItemKindId] = {
      val choices = assets.catalogues.items(assets.themes(area.theme).items)
        .apply(assets.catalogues.items)
        .filterKeys(_.isInstanceOf[Equipment.Category])
        .map(x => x._1.asInstanceOf[Equipment.Category] -> x._2)

      val categories = if (this.category.isEmpty) Equipment.categories else this.category
      val constraints = if (this.power.isEmpty) CombatPower.categories else this.power

      def getPower(category: Equipment.Category, choice: ItemKindId): Option[Int] = {
        assets.combatPower.equipment.get(category) flatMap (_.get(choice))
      }

      selectCategoryContentByPower(area, choices, categories, constraints, random, getPower)
    }
  }

  case class ThemeTemplate(category: Set[Template.Category] = Set(),
                           power: Set[CombatPower.Category] = Set(),
                           tags: Set[Tag] = Set()
                          ) extends TemplateSelection {
    def apply(assets: WorldAssets, area: Area, random: Random): Option[TemplateId] = {
      val choices = assets.catalogues.templates(assets.themes(area.theme).templates)
        .apply(assets.catalogues.templates)
      val categories = if (this.category.isEmpty) Template.categories else this.category
      val constraints = if (this.power.isEmpty) CombatPower.categories else this.power

      def getPower(category: Template.Category, choice: TemplateId): Option[Int] = {
        assets.templates(choice).power
      }

      selectCategoryContentByPower(area, choices, categories, constraints, random, getPower)
    }
  }

  case class ThemeTerrain(category: Set[TerrainKind.Category] = Set(),
                          tags: Set[Tag] = Set()
                         ) extends TerrainSelection {
    def apply(assets: WorldAssets, area: Area, random: Random): Option[TerrainKindId] = {
      val choices = assets.catalogues.terrains(assets.themes(area.theme).terrains)
        .apply(assets.catalogues.terrains)
      val categories: Set[TerrainKind.Category] = if (this.category.isEmpty) Set(TerrainKind.DefaultCategory) else this.category

      selectCategoryContent(area, choices, categories, random)
    }
  }

  case class ThemeWall(category: Set[WallKind.Category] = Set(),
                       tags: Set[Tag] = Set()
                      ) extends WallSelection {
    def apply(assets: WorldAssets, area: Area, random: Random): Option[WallKindId] = {
      val choices = assets.catalogues.walls(assets.themes(area.theme).walls)
        .apply(assets.catalogues.walls)
      val categories: Set[WallKind.Category] = if (this.category.isEmpty) Set(WallKind.DefaultCategory) else this.category

      selectCategoryContent(area, choices, categories, random)
    }
  }

  case class ThemeWidget(category: Set[WidgetKind.Category] = Set(),
                         power: Set[CombatPower.Category] = Set(),
                         tags: Set[Tag] = Set()
                        ) extends WidgetSelection {
    def apply(assets: WorldAssets, area: Area, random: Random): Option[WidgetKindId] = {
      val choices = assets.catalogues.widgets(assets.themes(area.theme).widgets)
        .apply(assets.catalogues.widgets)
      val categories = if (this.category.isEmpty) WidgetKind.categories else this.category
      val constraints = if (this.power.isEmpty) CombatPower.categories else this.power

      def getPower(category: WidgetKind.Category, choice: WidgetKindId): Option[Int] = {
        assets.kinds.widgets(choice).power
      }

      selectCategoryContentByPower(area, choices, categories, constraints, random, getPower)
    }
  }

  private def selectCategoryContent[T, U](area: Area,
                                          choices: Map[U, Iterable[WeightedChoice[T]]],
                                          categories: Set[U],
                                          random: Random
                                         ): Option[T] = {
    def select(categories: Set[U]): Option[T] = {
      if (categories.isEmpty) {
        None
      } else {
        val category = Rng.nextChoice(random, categories)
        val categoryChoices = choices.getOrElse(category, List())

        if (categoryChoices.isEmpty) {
          select(categories - category)
        } else {
          Some(Rng.nextChoice(random, WeightedChoices(categoryChoices)))
        }
      }
    }

    select(categories)
  }

  private def selectCategoryContentByPower[T, U](area: Area,
                                                 choices: Map[U, Iterable[WeightedChoice[T]]],
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
        val categoryChoices = choices.getOrElse(category, List())
        val result = selectContentByPower(area, categoryChoices, constraints, random, (choice: T) => getPower(category, choice))

        result orElse select(categories - category)
      }
    }

    select(categories)
  }

  private def selectContentByPower[T](area: Area,
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
