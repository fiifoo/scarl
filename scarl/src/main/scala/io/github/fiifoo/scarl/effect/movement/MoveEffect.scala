package io.github.fiifoo.scarl.effect.movement

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.geometry.{Location, Obstacle}

case class MoveEffect(target: CreatureId,
                      from: Location,
                      to: Location,
                      parent: Option[Effect] = None
                     ) extends Effect {

  def apply(s: State): EffectResult = {
    if (target(s).traits.immobile) {
      return EffectResult()
    }

    Obstacle.movement(s, this.target(s).traits.flying)(to) map (obstacle => {
      EffectResult(
        CollideEffect(target, obstacle, Some(this))
      )
    }) getOrElse {
      EffectResult(
        MovedEffect(target, from, to, Some(this))
      )
    }
  }
}
