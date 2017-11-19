package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}

case class ItemFinderIndexRemoveMutation(finder: CreatureId, items: Set[ItemId]) {
  type Index = Map[ItemId, Set[CreatureId]]

  def apply(index: Index): Index = {
    (items foldLeft index) ((index, item) => {
      val previous = index.get(item)
      val next = previous map (previous => previous - finder) getOrElse Set()

      if (next.isEmpty) {
        index - item
      } else {
        index + (item -> next)
      }
    })
  }
}
