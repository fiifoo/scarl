package io.github.fiifoo.scarl.action.validate

import io.github.fiifoo.scarl.action.EquipItemAction
import io.github.fiifoo.scarl.action.validate.ValidatorUtils._
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.{CreatureId, Item}
import io.github.fiifoo.scarl.core.item.Equipment.Slot
import io.github.fiifoo.scarl.core.item._

object EquipItemValidator {
  def apply(s: State, actor: CreatureId, action: EquipItemAction): Boolean = {
    if (!entityExists(s, action.item)) {
      return false
    }

    val item = action.item(s)

    item.container == actor && validateSlot(item, action.slot)
  }

  private def validateSlot(item: Item, slot: Slot): Boolean = {
    val slots = getSlots(item.armor) ++ getSlots(item.shield) ++ getSlots(item.rangedWeapon) ++ getSlots(item.weapon)

    slots contains slot
  }

  private def getSlots(equipment: Option[Equipment]): Set[Slot] = {
    equipment map (_.slots) getOrElse Set()
  }
}
