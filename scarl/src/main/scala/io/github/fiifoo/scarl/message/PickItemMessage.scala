package io.github.fiifoo.scarl.message

import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.effect.PickItemEffect

class PickItemMessage(player: () => CreatureId,
                      fov: () => Set[Location]
                     ) extends MessageBuilder[PickItemEffect] {

  def apply(s: State, effect: PickItemEffect): Option[String] = {
    val creature = effect.picker
    val item = effect.target

    if (creature == player()) {
      Some(s"You pick up ${kind(s, item)}.")
    } else if (fov() contains creature(s).location) {
      Some(s"${kind(s, creature)} picks up ${kind(s, item)}.")
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
