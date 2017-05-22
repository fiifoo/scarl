package io.github.fiifoo.scarl.message

import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId, WallId}
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.effect.CollideEffect

class CollideMessage(player: () => CreatureId,
                     fov: () => Set[Location]
                    ) extends MessageBuilder[CollideEffect] {

  def apply(s: State, effect: CollideEffect): Option[String] = {
    val target = effect.target

    if (target == player()) {
      effect.obstacle match {
        case _: ItemId => Some("Ouch. You run straight into door.")
        case _: WallId => Some("Ouch. You run straight into wall.")
        case c: CreatureId => Some(s"${kind(s, c)} blocks your way.")
        case _ => None
      }
    } else {
      None
    }
  }

  private def kind(s: State, creature: CreatureId): String = {
    s.kinds.creatures(creature(s).kind).name
  }
}
