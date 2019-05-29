package io.github.fiifoo.scarl.effect.movement

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId

case class DisplaceEffect(displacer: CreatureId,
                          displaced: CreatureId,
                          parent: Option[Effect] = None
                         ) extends Effect {

  def apply(s: State): EffectResult = {
    if (this.displaced(s).immobile) {
      EffectResult(
        CollideEffect(this.displacer, this.displaced, Some(this))
      )
    } else {
      EffectResult(
        DisplacedEffect(this.displacer, this.displaced, Some(this))
      )
    }
  }
}
