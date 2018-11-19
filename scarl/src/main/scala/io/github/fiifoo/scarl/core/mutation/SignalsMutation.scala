package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core._
import io.github.fiifoo.scarl.core.entity.Signal

case class SignalsMutation(signals: List[Signal]) extends Mutation {
  def apply(s: State): State = {
    s.copy(
      signals = signals
    )
  }
}
