package fi.fiifoo.scarl.core.mutation

import fi.fiifoo.scarl.core.State

trait Mutation {
  def apply(s: State): State
}
