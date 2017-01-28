package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State

case class SeedMutation(seed: Int) extends Mutation {

  def apply(s: State): State = {
    s.copy(seed = seed)
  }
}
