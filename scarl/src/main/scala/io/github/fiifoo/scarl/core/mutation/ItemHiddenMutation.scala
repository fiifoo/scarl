package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.ItemId

case class ItemHiddenMutation(item: ItemId, hidden: Boolean) extends Mutation {

  def apply(s: State): State = {
    val previous = item(s)
    val next = previous.copy(hidden = hidden)

    s.copy(
      entities = s.entities + (item -> next)
    )
  }
}
