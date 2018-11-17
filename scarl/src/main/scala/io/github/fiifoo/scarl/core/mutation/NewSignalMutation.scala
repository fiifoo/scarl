package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core._
import io.github.fiifoo.scarl.core.entity.Signal

case class NewSignalMutation(signal: Signal) extends Mutation {
  def apply(s: State): State = {
    s.copy(
      signals = signal :: s.signals
    )
  }
}
