package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.kind.ItemKindId

case class ItemRecycledMutation(item: ItemKindId, recycler: CreatureId) extends Mutation {
  def apply(s: State): State = {
    val previous = s.creature.recycledItems.getOrElse(this.recycler, List())
    val next = this.item :: previous

    s.copy(creature = s.creature.copy(
      recycledItems = s.creature.recycledItems + (this.recycler -> next)
    ))
  }
}
