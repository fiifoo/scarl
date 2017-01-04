package fi.fiifoo.scarl.core.mutation

import fi.fiifoo.scarl.core.State

case class ResetAddedActorsMutation() extends Mutation {

  def apply(s: State): State = {
    s.copy(tmp = s.tmp.copy(
      addedActors = List()
    ))
  }
}
