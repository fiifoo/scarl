package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.character.Stats
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import io.github.fiifoo.scarl.core.equipment._

case class EquipmentStatsIndexMutation(creature: CreatureId) {
  type Index = Map[CreatureId, Stats]

  def apply(s: State, index: Index): Index = {
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
          case _: ArmorSlot =>
            getStats(data.armor)
        }

        (result add stats, processed + item)
      }
    })

    index + (creature -> stats)
  }

  private def getStats(equipment: Option[Equipment]): Stats = {
    equipment map (_.stats) getOrElse Stats()
  }
}
