package io.github.fiifoo.scarl.core.test_assets

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.ActorId
import io.github.fiifoo.scarl.core.mutation.ActorTickMutation

case class TestTickEffect(target: ActorId,
                          amount: Int,
                          parent: Option[Effect] = None
                         ) extends Effect {

  def apply(s: State): EffectResult = {
    EffectResult(ActorTickMutation(target, target(s).tick + amount))
  }
}
