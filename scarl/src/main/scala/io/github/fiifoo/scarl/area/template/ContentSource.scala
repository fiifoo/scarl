package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.area.template.ContentSelection._
import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.core.item.Equipment
import io.github.fiifoo.scarl.core.kind.{CreatureKindId, ItemKindId, WidgetKindId}
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
        case selection: ThemeCreature => selectThemeCreature(assets, area, selection, random)
        case selection: FixedCreature => selection.kind
      }
    }

    private def selectThemeCreature(assets: WorldAssets,
                                    area: Area,
                                    selection: ThemeCreature,
                                    random: Random
                                   ): CreatureKindId = {
      val constraints = if (selection.power.isEmpty) CombatPower.categories else selection.power
      val constraint = Rng.nextChoice(random, constraints)

      val choices = area.combatPower.get(constraint) map (x => {
        val (min, max) = x

        assets.themes(area.theme).creatures filter (creature => {
          val power = assets.combatPower.average.get(creature)

          power.exists(p => p >= min && p <= max)
        })
      }) getOrElse Set()

      if (choices.isEmpty) {
        throw new CalculateFailedException
      }

      Rng.nextChoice(random, choices)
    }
  }

  case class ItemSource(selection: ItemSelection,
                        distribution: Distribution
                       ) extends ContentSource[ItemKindId] {
    def apply(assets: WorldAssets, area: Area, random: Random): ItemKindId = {
      selection match {
        case selection: ThemeEquipment => selectThemeEquipment(assets, area, selection, random)
        case selection: FixedItem => selection.kind
      }
    }

    private def selectThemeEquipment(assets: WorldAssets,
                                     area: Area,
                                     selection: ThemeEquipment,
                                     random: Random
                                    ): ItemKindId = {
      val categories = if (selection.category.isEmpty) Equipment.categories else selection.category
      val category = Rng.nextChoice(random, categories)
      val constraints = if (selection.power.isEmpty) CombatPower.categories else selection.power
      val constraint = Rng.nextChoice(random, constraints)

      val choices = area.combatPower.get(constraint) map (x => {
        val (min, max) = x

        assets.themes(area.theme).items filter (item => {
          val power = assets.equipmentCombatPower.get(category) flatMap (_.get(item))

          power.exists(p => p >= min && p <= max)
        })
      }) getOrElse Set()

      if (choices.isEmpty) {
        throw new CalculateFailedException
      }

      Rng.nextChoice(random, choices)
    }
  }

  case class WidgetSource(selection: WidgetKindId, distribution: Distribution) extends ContentSource[WidgetKindId] {
    def apply(assets: WorldAssets, area: Area, random: Random): WidgetKindId = selection
  }

}
