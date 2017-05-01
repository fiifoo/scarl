package io.github.fiifoo.scarl.message

import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.effect.EquipItemEffect

class EquipItemMessage(player: () => CreatureId,
                       fov: () => Set[Location]
                      ) extends MessageBuilder[EquipItemEffect] {

  def apply(s: State, effect: EquipItemEffect): Option[String] = {
    val creature = effect.creature
    val item = effect.item

    if (effect.parent.exists(_.isInstanceOf[EquipItemEffect])) {
      None
    } else if (creature == player()) {
      Some(s"You equip ${kind(s, item)}.")
    }
    else if (fov() contains creature(s).location) {
      Some(s"${kind(s, creature)} equips ${kind(s, item)}.")
    } else {
      None
    }
  }

  private def kind(s: State, creature: CreatureId): String = {
    s.kinds.creatures(creature(s).kind).name
  }

  private def kind(s: State, item: ItemId): String = {
    s.kinds.items(item(s).kind).name
  }
}
