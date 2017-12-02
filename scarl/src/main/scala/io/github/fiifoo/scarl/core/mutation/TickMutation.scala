package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.Time.Tick

case class TickMutation(tick: Tick) extends Mutation {

  def apply(s: State): State = {
    s.copy(tick = tick)
  }
}
