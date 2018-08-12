package io.github.fiifoo.scarl.effect.movement

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.mutation.LocatableLocationMutation

case class DisplaceEffect(displacer: CreatureId,
                          displaced: CreatureId,
                          parent: Option[Effect] = None
                         ) extends Effect {

  def apply(s: State): EffectResult = {
    val displacer = this.displacer(s)
    val displaced = this.displaced(s)

    if (displacer.immobile || displaced.immobile) {
      return EffectResult()
    }

    val from = displacer.location
    val to = displaced.location

    EffectResult(
      LocatableLocationMutation(this.displaced, from),
      MovedEffect(this.displacer, from, to, Some(this))
    )
  }
}
