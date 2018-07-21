package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.LockableId
import io.github.fiifoo.scarl.core.item.Lock

case class LockableLockedMutation(lockable: LockableId, locked: Option[Lock]) extends Mutation {
  def apply(s: State): State = {
    val previous = lockable(s)
    val next = previous.setLocked(locked)

    s.copy(
      entities = s.entities + (lockable -> next)
    )
  }
}
