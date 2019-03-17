package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.ConditionStatus

case class ConditionStrengthMutation(status: ConditionStatus, strength: Int) extends Mutation {

  def apply(s: State): State = {
    s.copy(entities = s.entities + (status.id -> status.setStrength(strength)))
  }
}
