package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State

case class TickMutation(tick: Int) extends Mutation {

  def apply(s: State): State = {
    s.copy(tick = tick)
  }
}
