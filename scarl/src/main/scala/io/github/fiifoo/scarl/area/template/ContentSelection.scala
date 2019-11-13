package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.core.Tag
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
                           tags: Set[Tag] = Set()
                          ) extends CreatureSelection {
    def apply(assets: WorldAssets, area: Area, random: Random): Option[CreatureKindId] = {
      val choices = assets.catalogues.creatures(assets.themes(area.theme).creatures)
        .apply(assets.catalogues.creatures)
      val categories: Set[CreatureKind.Category] = if (this.category.isEmpty) Set(CreatureKind.DefaultCategory) else this.category

      selectCategoryContent(area, choices, categories, random)
    }
  }

  case class ThemeDoor(category: Set[ItemKind.DoorCategory] = Set(),
                       tags: Set[Tag] = Set()
                      ) extends DoorSelection {
    def apply(assets: WorldAssets, area: Area, random: Random): Option[ItemKindId] = {
      val choices = assets.catalogues.items(assets.themes(area.theme).items)
        .apply(assets.catalogues.items)
        .filter(_._1.isInstanceOf[ItemKind.DoorCategory])
        .map(x => x._1.asInstanceOf[ItemKind.DoorCategory] -> x._2)
      val categories: Set[ItemKind.DoorCategory] = if (this.category.isEmpty) Set(ItemKind.DefaultDoorCategory) else this.category

      selectCategoryContent(area, choices, categories, random)
    }
  }

  case class ThemeItem(category: Set[ItemKind.Category] = Set(),
                       tags: Set[Tag] = Set()
                      ) extends ItemSelection {
    def apply(assets: WorldAssets, area: Area, random: Random): Option[ItemKindId] = {
      val choices = assets.catalogues.items(assets.themes(area.theme).items)
        .apply(assets.catalogues.items)
      val categories: Set[ItemKind.Category] = if (this.category.isEmpty) Set(ItemKind.UtilityCategory) else this.category

      selectCategoryContent(area, choices, categories, random)
    }
  }

  case class ThemeEquipment(category: Set[Equipment.Category] = Set(),
                            tags: Set[Tag] = Set()
                           ) extends ItemSelection {
    def apply(assets: WorldAssets, area: Area, random: Random): Option[ItemKindId] = {
      val choices = assets.catalogues.items(assets.themes(area.theme).items)
        .apply(assets.catalogues.items)
        .filter(_._1.isInstanceOf[Equipment.Category])
        .map(x => x._1.asInstanceOf[Equipment.Category] -> x._2)
      val categories = if (this.category.isEmpty) Equipment.categories else this.category

      selectCategoryContent(area, choices, categories, random)
    }
  }

  case class ThemeTemplate(category: Set[Template.Category] = Set(),
                           tags: Set[Tag] = Set()
                          ) extends TemplateSelection {
    def apply(assets: WorldAssets, area: Area, random: Random): Option[TemplateId] = {
      val choices = assets.catalogues.templates(assets.themes(area.theme).templates)
        .apply(assets.catalogues.templates)
      val categories = if (this.category.isEmpty) Template.categories else this.category

      selectCategoryContent(area, choices, categories, random)
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
                         tags: Set[Tag] = Set()
                        ) extends WidgetSelection {
    def apply(assets: WorldAssets, area: Area, random: Random): Option[WidgetKindId] = {
      val choices = assets.catalogues.widgets(assets.themes(area.theme).widgets)
        .apply(assets.catalogues.widgets)
      val categories = if (this.category.isEmpty) WidgetKind.categories else this.category

      selectCategoryContent(area, choices, categories, random)
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
}
