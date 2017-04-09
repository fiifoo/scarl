package io.github.fiifoo.scarl.message

import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.effect.HitEffect

class HitMessage(player: () => CreatureId,
                 fov: () => Set[Location]
                ) extends MessageBuilder[HitEffect] {

  def apply(s: State, effect: HitEffect): Option[String] = {
    val bypass = effect.result.bypass

    if (effect.attacker == player()) {
      Some(if (bypass.isDefined) {
        s"You hit ${kind(s, effect.target)} bypassing some of it's armor."
      } else {
        s"You hit ${kind(s, effect.target)}."
      })
    } else if (effect.target == player()) {
      Some(if (bypass.isDefined) {
        s"${kind(s, effect.attacker)} hits you bypassing some of your armor."
      } else {
        s"${kind(s, effect.attacker)} hits you."
      })
    } else if (fov() contains effect.target(s).location) {
      Some(if (bypass.isDefined) {
        s"${kind(s, effect.attacker)} hits ${kind(s, effect.target)} bypassing some of it's armor."
      } else {
        s"${kind(s, effect.attacker)} hits ${kind(s, effect.target)}."
      })
    } else {
      None
    }
  }

  private def kind(s: State, creature: CreatureId): String = {
    s.kinds.creatures(creature(s).kind).name
  }

}
