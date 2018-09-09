package io.github.fiifoo.scarl.action.validate

import io.github.fiifoo.scarl.action.validate.ValidatorUtils._
import io.github.fiifoo.scarl.action.{EquipItemAction, EquipWeaponsAction}
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.{CreatureId, Item, ItemId}
import io.github.fiifoo.scarl.core.item.Equipment.{ArmorSlot, Slot}
import io.github.fiifoo.scarl.core.item._

object EquipItemValidator {
  def apply(s: State, actor: CreatureId, action: EquipItemAction): Boolean = {
    validateItem(s, actor, action.item, action.slot)
  }

  def apply(s: State, actor: CreatureId, action: EquipWeaponsAction): Boolean = {
    if (action.weapons.keys exists (_.isInstanceOf[ArmorSlot])) {
      return false
    }

    val invalid = action.weapons exists (x => {
      val (slot, item) = x

      !validateItem(s, actor, item, slot)
    })

    !invalid
  }

  private def validateItem(s: State, actor: CreatureId, item: ItemId, slot: Slot): Boolean = {
    if (!entityExists(s)(item)) {
      return false
    }

    item(s).container == actor && validateSlot(item(s), slot)
  }

  private def validateSlot(item: Item, slot: Slot): Boolean = {
    val slots = getSlots(item.armor) ++
      getSlots(item.launcher) ++
      getSlots(item.rangedWeapon) ++
      getSlots(item.shield) ++
      getSlots(item.weapon)

    slots contains slot
  }

  private def getSlots(equipment: Option[Equipment]): Set[Slot] = {
    equipment map (_.slots) getOrElse Set()
  }
}
