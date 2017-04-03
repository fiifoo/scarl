package io.github.fiifoo.scarl.message

import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.effect.DeathEffect

class DeathMessage(player: () => CreatureId, fov: () => Set[Location]) {

  def apply(s: State, effect: DeathEffect): Option[String] = {
    val target = effect.target

    if (target == player()) {
      Some("You die...")
    } else if (fov() contains target(s).location) {
      Some(s"${kind(s, target)} is killed.")
    } else {
      None
    }
  }

  private def kind(s: State, creature: CreatureId): String = {
    s.kinds.creatures(creature(s).kind).name
  }
}
