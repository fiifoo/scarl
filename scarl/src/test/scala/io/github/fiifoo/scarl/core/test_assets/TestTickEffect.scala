package io.github.fiifoo.scarl.core.test_assets

import io.github.fiifoo.scarl.core.Time.Tick
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.ActorId
import io.github.fiifoo.scarl.core.mutation.ActorTickMutation
import io.github.fiifoo.scarl.core.{State, Time}

case class TestTickEffect(target: ActorId,
                          amount: Tick = Time.turn,
                          parent: Option[Effect] = None
                         ) extends Effect {

  def apply(s: State): EffectResult = {
    EffectResult(
      ActorTickMutation(target, target(s).tick + amount)
    )
  }
}
