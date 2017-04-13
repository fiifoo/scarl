package io.github.fiifoo.scarl.message

import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.effect.GainLevelEffect

class GainLevelMessage(player: () => CreatureId,
                       fov: () => Set[Location]
                      ) extends MessageBuilder[GainLevelEffect] {

  def apply(s: State, effect: GainLevelEffect): Option[String] = {
    val target = effect.target

    if (target == player()) {
      Some("You feel stronger.")
    } else if (fov() contains target(s).location) {
      Some(s"${kind(s, target)} looks stronger.")
    } else {
      None
    }
  }

  private def kind(s: State, creature: CreatureId): String = {
    s.kinds.creatures(creature(s).kind).name
  }
}
