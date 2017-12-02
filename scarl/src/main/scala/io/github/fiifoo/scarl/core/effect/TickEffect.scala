package io.github.fiifoo.scarl.core.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.Time.Tick
import io.github.fiifoo.scarl.core.entity.ActorId
import io.github.fiifoo.scarl.core.mutation.ActorTickMutation

case class TickEffect(target: ActorId,
                      amount: Tick,
                      parent: Option[Effect] = None
                     ) extends Effect {

  def apply(s: State): EffectResult = {
    EffectResult(ActorTickMutation(target, target(s).tick + amount))
  }
}
