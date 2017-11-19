package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}

case class ItemFinderIndexAddMutation(finder: CreatureId, item: ItemId) {
  type Index = Map[ItemId, Set[CreatureId]]

  def apply(index: Index): Index = {
    val previous = index.get(item)
    val next = previous map (previous => previous + finder) getOrElse Set(finder)

    index + (item -> next)
  }
}
