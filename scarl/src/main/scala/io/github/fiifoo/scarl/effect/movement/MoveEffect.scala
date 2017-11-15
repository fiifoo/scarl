package io.github.fiifoo.scarl.effect.movement

import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.geometry.Obstacle

case class MoveEffect(target: CreatureId,
                      from: Location,
                      to: Location,
                      parent: Option[Effect] = None
                     ) extends Effect {

  def apply(s: State): EffectResult = {
    Obstacle.movement(s)(to) map (obstacle => {
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
