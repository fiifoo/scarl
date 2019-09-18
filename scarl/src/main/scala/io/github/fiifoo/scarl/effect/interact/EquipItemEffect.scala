package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item.Equipment.Slot
import io.github.fiifoo.scarl.core.item._
import io.github.fiifoo.scarl.core.mutation.EquipItemMutation

/**
  * Equips given item & unequips items from needed slots
  *
  * If unequip is needed will create UnequipItemEffects and new EquipItemEffect to get correct order for mutations
  */
case class EquipItemEffect(creature: CreatureId,
                           item: ItemId,
                           slot: Slot,
                           location: Location,
                           parent: Option[Effect] = None
                          ) extends Effect {

  def apply(s: State): EffectResult = {
    Equipment.selectEquipment(slot, item(s)) map (equipment => {

      parent collect {
        case _: EquipItemEffect => getEquipResult(equipment)
      } getOrElse {
        val unequip = getUnequipItems(s, equipment)

        if (unequip.isEmpty) {
          getEquipResult(equipment)
        } else {
          getUnequipResult(unequip)
        }
      }

    }) getOrElse EffectResult()
  }

  private def getUnequipItems(s: State, equipment: Equipment): List[ItemId] = {
    val equipments = s.creature.equipments.getOrElse(creature, Map())

    val unequip = if (equipment.fillAll) {
      (equipments.view filterKeys equipment.slots.contains).values.toSet
    } else {
      equipments.get(slot) map (Set(_)) getOrElse Set()
    }

    if (equipments.values exists (_ == item)) {
      (unequip + item).toList
    } else {
      unequip.toList
    }
  }

  private def getEquipResult(equipment: Equipment): EffectResult = {
    val slots = if (equipment.fillAll) equipment.slots else Set(slot)

    EffectResult(EquipItemMutation(creature, item, slots))
  }

  private def getUnequipResult(unequipItems: List[ItemId]): EffectResult = {
    val unequipEffects = unequipItems map (item => {
      UnequipItemEffect(creature, item, location, Some(this))
    })
    val equipEffect = EquipItemEffect(creature, item, slot, location, Some(this))

    EffectResult(
      unequipEffects ::: List(equipEffect)
    )

  }
}
