package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.ItemId
import io.github.fiifoo.scarl.core.item.Key

case class ItemLockedMutation(item: ItemId, locked: Option[Key]) extends Mutation {
  def apply(s: State): State = {
    val previous = item(s)
    val next = previous.copy(locked = locked)

    s.copy(
      entities = s.entities + (item -> next)
    )
  }
}