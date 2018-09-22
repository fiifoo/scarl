package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.Selectors.getItemLocation
import io.github.fiifoo.scarl.core.entity.{Item, LockableId}
import io.github.fiifoo.scarl.core.item.Lock

case class LockableLockedMutation(lockable: LockableId, locked: Option[Lock]) extends Mutation {
  def apply(s: State): State = {
    val previous = lockable(s)
    val next = previous.setLocked(locked)

    val tmp = next match {
      case item: Item if item.door.exists(!_.open) => s.tmp.copy(
        waypointNetworkChanged = getItemLocation(s)(item.id) map (s.tmp.waypointNetworkChanged + _) getOrElse
          s.tmp.waypointNetworkChanged
      )
      case _ => s.tmp
    }

    s.copy(
      entities = s.entities + (lockable -> next),
      tmp = tmp
    )
  }
}
