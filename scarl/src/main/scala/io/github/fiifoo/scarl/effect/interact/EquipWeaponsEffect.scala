package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import io.github.fiifoo.scarl.core.item.Equipment._

case class EquipWeaponsEffect(creature: CreatureId,
                              weapons: Map[Slot, ItemId],
                              parent: Option[Effect] = None
                             ) extends Effect {

  private val slots: List[Slot] = List(
    MainHand,
    OffHand,
    RangedSlot,
    LauncherSlot,
  )

  def apply(s: State): EffectResult = {
    val location = this.creature(s).location
    val equipments = s.creature.equipments.getOrElse(this.creature, Map())

    val unequips = (this.slots flatMap equipments.get).distinct map (item => {
      UnequipItemEffect(this.creature, item, location, Some(this))
    })

    val equips = (this.weapons map (x => {
      val (slot, item) = x

      EquipItemEffect(this.creature, item, slot, location, Some(this))
    })).toList

    EffectResult(unequips ::: equips)
  }
}
