package io.github.fiifoo.scarl.core.mutation.cache

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Stats
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import io.github.fiifoo.scarl.core.item.Equipment._
import io.github.fiifoo.scarl.core.item._

case class EquipmentStatsCacheMutation(creature: CreatureId) {
  type Cache = Map[CreatureId, Stats]

  def apply(s: State, cache: Cache): Cache = {
    val equipments = s.equipments.getOrElse(creature, Map())

    val fold = equipments.foldLeft((Stats(), Set[ItemId]())) _

    val (stats, _) = fold((carry, element) => {
      val (result, processed) = carry
      val (slot, item) = element

      if (processed.contains(item)) {
        (result, processed)
      } else {
        val data = item(s)

        val stats = slot match {
          case _: HandSlot =>
            getStats(data.weapon) add getStats(data.shield)
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

    cache + (creature -> stats)
  }

  private def getStats(equipment: Option[Equipment]): Stats = {
    equipment map (_.stats) getOrElse Stats()
  }
}
