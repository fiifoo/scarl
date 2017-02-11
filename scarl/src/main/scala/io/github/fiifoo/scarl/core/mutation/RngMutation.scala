package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.{Rng, State}

case class RngMutation(rng: Rng) extends Mutation {

  def apply(s: State): State = {
    s.copy(rng = rng)
  }
}
