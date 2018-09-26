package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.kind.ItemKindId

case class ItemRecycleCanceledMutation(item: ItemKindId, recycler: CreatureId) extends Mutation {

  def apply(s: State): State = {
    val previous = s.creature.recycledItems.getOrElse(this.recycler, List())
    val index = previous.indexOf(this.item)
    val next = previous.zipWithIndex filter (_._2 != index) map (_._1)

    s.copy(creature = s.creature.copy(
      recycledItems = s.creature.recycledItems + (this.recycler -> next)
    ))
  }
}
