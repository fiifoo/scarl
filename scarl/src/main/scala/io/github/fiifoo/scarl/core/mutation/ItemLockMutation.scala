package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.ItemId
import io.github.fiifoo.scarl.core.item.Key

case class ItemLockMutation(item: ItemId, lock: Option[Key]) extends Mutation {
  def apply(s: State): State = {
    val previous = item(s)
    val next = previous.copy(lock = lock)

    s.copy(
      entities = s.entities + (item -> next)
    )
  }
}
