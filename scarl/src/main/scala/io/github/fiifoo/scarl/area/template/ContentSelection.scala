package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.template.Template.Context
import io.github.fiifoo.scarl.area.theme.ThemeId
import io.github.fiifoo.scarl.core.Tag
import io.github.fiifoo.scarl.core.assets._
import io.github.fiifoo.scarl.core.item.Equipment
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.core.math.Rng.{WeightedChoice, WeightedChoices}
import io.github.fiifoo.scarl.world.{TemplateCatalogueId, WorldAssets}

import scala.util.Random

sealed trait ContentSelection[T] {
  def apply(assets: WorldAssets, context: Context, random: Random): Option[T]

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

  case class CatalogueCreature(catalogue: CreatureCatalogueId,
                               category: Set[CreatureKind.Category] = Set(),
                               tags: Set[Tag] = Set()
                              ) extends CreatureSelection {
    def apply(assets: WorldAssets, context: Context, random: Random): Option[CreatureKindId] = {
      val choices = assets.catalogues.creatures(this.catalogue)
        .apply(assets.catalogues.creatures)
      val categories: Set[CreatureKind.Category] =
        if (this.category.isEmpty) Set(CreatureKind.DefaultCategory)
        else this.category

      selectCategoryContent(choices, categories, random)
    }
  }

  case class CatalogueDoor(catalogue: ItemCatalogueId,
                           category: Set[ItemKind.DoorCategory] = Set(),
                           tags: Set[Tag] = Set()
                          ) extends DoorSelection {
    def apply(assets: WorldAssets, context: Context, random: Random): Option[ItemKindId] = {
      val choices = assets.catalogues.items(this.catalogue)
        .apply(assets.catalogues.items)
        .filter(_._1.isInstanceOf[ItemKind.DoorCategory])
        .map(x => x._1.asInstanceOf[ItemKind.DoorCategory] -> x._2)
      val categories: Set[ItemKind.DoorCategory] =
        if (this.category.isEmpty) Set(ItemKind.DefaultDoorCategory)
        else this.category

      selectCategoryContent(choices, categories, random)
    }
  }

  case class CatalogueEquipment(catalogue: ItemCatalogueId,
                                category: Set[Equipment.Category] = Set(),
                                tags: Set[Tag] = Set()
                               ) extends ItemSelection {
    def apply(assets: WorldAssets, context: Context, random: Random): Option[ItemKindId] = {
      val choices = assets.catalogues.items(this.catalogue)
        .apply(assets.catalogues.items)
        .filter(_._1.isInstanceOf[Equipment.Category])
        .map(x => x._1.asInstanceOf[Equipment.Category] -> x._2)
      val categories =
        if (this.category.isEmpty) Equipment.categories
        else this.category

      selectCategoryContent(choices, categories, random)
    }
  }

  case class CatalogueItem(catalogue: ItemCatalogueId,
                           category: Set[ItemKind.Category] = Set(),
                           tags: Set[Tag] = Set()
                          ) extends ItemSelection {
    def apply(assets: WorldAssets, context: Context, random: Random): Option[ItemKindId] = {
      val choices = assets.catalogues.items(this.catalogue)
        .apply(assets.catalogues.items)
      val categories: Set[ItemKind.Category] =
        if (this.category.isEmpty) Set(ItemKind.UtilityCategory)
        else this.category

      selectCategoryContent(choices, categories, random)
    }
  }

  case class CatalogueTemplate(catalogue: TemplateCatalogueId,
                               category: Set[Template.Category] = Set(),
                               tags: Set[Tag] = Set()
                              ) extends TemplateSelection {
    def apply(assets: WorldAssets, context: Context, random: Random): Option[TemplateId] = {
      val choices = assets.catalogues.templates(this.catalogue)
        .apply(assets.catalogues.templates)
        .transform((_, choices) => {
          choices filter (choice => {
            val template = assets.templates(choice.value)

            !template.unique || !context.usedUniqueTemplates.contains(template.id)
          })
        })
      val categories =
        if (this.category.isEmpty) Template.categories
        else this.category

      selectCategoryContent(choices, categories, random)
    }
  }

  case class CatalogueTerrain(catalogue: TerrainCatalogueId,
                              category: Set[TerrainKind.Category] = Set(),
                              tags: Set[Tag] = Set()
                             ) extends TerrainSelection {
    def apply(assets: WorldAssets, context: Context, random: Random): Option[TerrainKindId] = {
      val choices = assets.catalogues.terrains(this.catalogue)
        .apply(assets.catalogues.terrains)
      val categories: Set[TerrainKind.Category] =
        if (this.category.isEmpty) Set(TerrainKind.DefaultCategory)
        else this.category

      selectCategoryContent(choices, categories, random)
    }
  }

  case class CatalogueWall(catalogue: WallCatalogueId,
                           category: Set[WallKind.Category] = Set(),
                           tags: Set[Tag] = Set()
                          ) extends WallSelection {
    def apply(assets: WorldAssets, context: Context, random: Random): Option[WallKindId] = {
      val choices = assets.catalogues.walls(this.catalogue)
        .apply(assets.catalogues.walls)
      val categories: Set[WallKind.Category] =
        if (this.category.isEmpty) Set(WallKind.DefaultCategory)
        else this.category

      selectCategoryContent(choices, categories, random)
    }
  }

  case class CatalogueWidget(catalogue: WidgetCatalogueId,
                             category: Set[WidgetKind.Category] = Set(),
                             tags: Set[Tag] = Set()
                            ) extends WidgetSelection {
    def apply(assets: WorldAssets, context: Context, random: Random): Option[WidgetKindId] = {
      val choices = assets.catalogues.widgets(this.catalogue)
        .apply(assets.catalogues.widgets)
      val categories =
        if (this.category.isEmpty) WidgetKind.categories
        else this.category

      selectCategoryContent(choices, categories, random)
    }
  }

  case class FixedCreature(kind: CreatureKindId, tags: Set[Tag] = Set()) extends CreatureSelection {
    def apply(assets: WorldAssets, context: Context, random: Random): Option[CreatureKindId] = Some(this.kind)
  }

  case class FixedDoor(kind: ItemKindId, tags: Set[Tag] = Set()) extends DoorSelection {
    def apply(assets: WorldAssets, context: Context, random: Random): Option[ItemKindId] = Some(this.kind)
  }

  case class FixedItem(kind: ItemKindId, tags: Set[Tag] = Set()) extends ItemSelection {
    def apply(assets: WorldAssets, context: Context, random: Random): Option[ItemKindId] = Some(this.kind)
  }

  case class FixedTemplate(template: TemplateId) extends TemplateSelection {
    val tags: Set[Tag] = Set()

    def apply(assets: WorldAssets, context: Context, random: Random): Option[TemplateId] = Some(this.template)
  }

  case class FixedTerrain(kind: TerrainKindId, tags: Set[Tag] = Set()) extends TerrainSelection {
    def apply(assets: WorldAssets, context: Context, random: Random): Option[TerrainKindId] = Some(this.kind)
  }

  case class FixedWall(kind: WallKindId, tags: Set[Tag] = Set()) extends WallSelection {
    def apply(assets: WorldAssets, context: Context, random: Random): Option[WallKindId] = Some(this.kind)
  }

  case class FixedWidget(kind: WidgetKindId, tags: Set[Tag] = Set()) extends WidgetSelection {
    def apply(assets: WorldAssets, context: Context, random: Random): Option[WidgetKindId] = Some(this.kind)
  }

  case class ThemeCreature(theme: Option[ThemeId] = None,
                           category: Set[CreatureKind.Category] = Set(),
                           tags: Set[Tag] = Set()
                          ) extends CreatureSelection {
    def apply(assets: WorldAssets, context: Context, random: Random): Option[CreatureKindId] = {
      val catalogue = assets.themes(this.theme getOrElse context.theme).creatures

      CatalogueCreature(catalogue, this.category, this.tags).apply(assets, context, random)
    }
  }

  case class ThemeDoor(theme: Option[ThemeId] = None,
                       category: Set[ItemKind.DoorCategory] = Set(),
                       tags: Set[Tag] = Set()
                      ) extends DoorSelection {
    def apply(assets: WorldAssets, context: Context, random: Random): Option[ItemKindId] = {
      val catalogue = assets.themes(this.theme getOrElse context.theme).items

      CatalogueDoor(catalogue, this.category, this.tags).apply(assets, context, random)
    }
  }

  case class ThemeEquipment(theme: Option[ThemeId] = None,
                            category: Set[Equipment.Category] = Set(),
                            tags: Set[Tag] = Set()
                           ) extends ItemSelection {
    def apply(assets: WorldAssets, context: Context, random: Random): Option[ItemKindId] = {
      val catalogue = assets.themes(this.theme getOrElse context.theme).items

      CatalogueEquipment(catalogue, this.category, this.tags).apply(assets, context, random)
    }
  }

  case class ThemeItem(theme: Option[ThemeId] = None,
                       category: Set[ItemKind.Category] = Set(),
                       tags: Set[Tag] = Set()
                      ) extends ItemSelection {
    def apply(assets: WorldAssets, context: Context, random: Random): Option[ItemKindId] = {
      val catalogue = assets.themes(this.theme getOrElse context.theme).items

      CatalogueItem(catalogue, this.category, this.tags).apply(assets, context, random)
    }
  }

  case class ThemeTemplate(theme: Option[ThemeId] = None,
                           category: Set[Template.Category] = Set(),
                           tags: Set[Tag] = Set()
                          ) extends TemplateSelection {
    def apply(assets: WorldAssets, context: Context, random: Random): Option[TemplateId] = {
      val catalogue = assets.themes(this.theme getOrElse context.theme).templates

      CatalogueTemplate(catalogue, this.category, this.tags).apply(assets, context, random)
    }
  }

  case class ThemeTerrain(theme: Option[ThemeId] = None,
                          category: Set[TerrainKind.Category] = Set(),
                          tags: Set[Tag] = Set()
                         ) extends TerrainSelection {
    def apply(assets: WorldAssets, context: Context, random: Random): Option[TerrainKindId] = {
      val catalogue = assets.themes(this.theme getOrElse context.theme).terrains

      CatalogueTerrain(catalogue, this.category, this.tags).apply(assets, context, random)
    }
  }

  case class ThemeWall(theme: Option[ThemeId] = None,
                       category: Set[WallKind.Category] = Set(),
                       tags: Set[Tag] = Set()
                      ) extends WallSelection {
    def apply(assets: WorldAssets, context: Context, random: Random): Option[WallKindId] = {
      val catalogue = assets.themes(this.theme getOrElse context.theme).walls

      CatalogueWall(catalogue, this.category, this.tags).apply(assets, context, random)
    }
  }

  case class ThemeWidget(theme: Option[ThemeId] = None,
                         category: Set[WidgetKind.Category] = Set(),
                         tags: Set[Tag] = Set()
                        ) extends WidgetSelection {
    def apply(assets: WorldAssets, context: Context, random: Random): Option[WidgetKindId] = {
      val catalogue = assets.themes(this.theme getOrElse context.theme).widgets

      CatalogueWidget(catalogue, this.category, this.tags).apply(assets, context, random)
    }
  }

  private def selectCategoryContent[T, U](choices: Map[U, Iterable[WeightedChoice[T]]],
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
