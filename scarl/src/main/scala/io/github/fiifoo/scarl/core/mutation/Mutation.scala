package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State

trait Mutation {
  def apply(s: State): State
}
