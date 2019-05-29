package io.github.fiifoo.scarl.effect.movement

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.mutation.LocatableLocationMutation

case class DisplacedEffect(displacer: CreatureId,
                           displaced: CreatureId,
                           parent: Option[Effect] = None
                          ) extends Effect {

  def apply(s: State): EffectResult = {
    val from = this.displacer(s).location
    val to = this.displaced(s).location

    EffectResult(
      LocatableLocationMutation(this.displaced, from),
      MovedEffect(this.displacer, from, to, Some(this))
    )
  }
}
