package io.github.fiifoo.scarl.rule

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Stats
import io.github.fiifoo.scarl.core.creature.Stats.{Consumption, Melee}
import io.github.fiifoo.scarl.core.entity.ItemId
import io.github.fiifoo.scarl.core.item.Equipment._
import io.github.fiifoo.scarl.core.item.{Equipment, Weapon}

object EquipmentStatsRule {

  private val offHandDivisor = 2

  def apply(s: State, equipments: Map[Slot, ItemId]): Stats = {
    val initial: (Stats, Set[ItemId]) = (Stats(), Set[ItemId]())

    val (stats, _) = (equipments foldLeft initial) ((carry, element) => {
      val (result, processed) = carry
      val (slot, item) = element

      if (processed.contains(item)) {
        (result, processed)
      } else {
        val data = item(s)

        val stats = slot match {
          case MainHand =>
            getStats(data.weapon)
          case OffHand =>
            getOffHandWeaponStats(data.weapon) add getStats(data.shield)
          case RangedSlot =>
            getStats(data.rangedWeapon)
          case LauncherSlot =>
            getStats(data.launcher)
          case _: ArmorSlot =>
            getStats(data.armor)
        }

        (result add stats, processed + item)
      }
    })

    stats
  }

  private def getStats(equipment: Option[Equipment]): Stats = {
    equipment map (_.stats) getOrElse Stats()
  }

  private def getOffHandWeaponStats(weapon: Option[Weapon]): Stats = {
    weapon map (weapon => {
      if (weapon.twoHanded) {
        weapon.stats
      } else {
        val melee = weapon.stats.melee
        val consumption = melee.consumption

        weapon.stats.copy(melee = Melee(
          melee.attack / offHandDivisor,
          melee.damage / offHandDivisor,
          consumption = Consumption(
            consumption.energy / offHandDivisor,
            consumption.materials / offHandDivisor
          )
        ))
      }
    }) getOrElse {
      Stats()
    }
  }
}
