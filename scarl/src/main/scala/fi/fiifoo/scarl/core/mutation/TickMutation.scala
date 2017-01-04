package fi.fiifoo.scarl.core.mutation

import fi.fiifoo.scarl.core.State

case class TickMutation(tick: Int) extends Mutation {

  def apply(s: State): State = {
    s.copy(tick = tick)
  }
}
