package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.StanceStatus

case class StanceActiveMutation(status: StanceStatus, active: Boolean) extends Mutation {

  def apply(s: State): State = {
    s.copy(entities = s.entities + (status.id -> status.setActive(active)))
  }
}
