package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.math.Rng

case class RngMutation(rng: Rng) extends Mutation {

  def apply(s: State): State = {
    s.copy(rng = rng)
  }
}
