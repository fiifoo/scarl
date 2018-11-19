package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.GlobalActorId
import io.github.fiifoo.scarl.effect.TickEffect
import io.github.fiifoo.scarl.effect.area.SignalsEffect
import io.github.fiifoo.scarl.rule.SignalRule

object GlobalEffects {
  def apply(s: State, actor: GlobalActorId): List[Effect] = {
    List(
      Some(TickEffect(actor, Time.turn)),
      signals(s, actor(s).tick)
    ).flatten
  }

  private def signals(s: State, tick: Int): Option[Effect] = {
    val limit = tick - SignalRule.SignalDuration

    val signals = s.signals filter (_.tick >= limit)

    if (signals.size != s.signals.size) {
      Some(SignalsEffect(signals))
    } else {
      None
    }
  }
}
