package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import io.github.fiifoo.scarl.core.mutation.index.ItemFinderIndexAddMutation

case class ItemFoundMutation(item: ItemId, finder: CreatureId) extends Mutation {

  def apply(s: State): State = {
    val previous = s.creature.foundItems.get(finder)
    val next = previous map (previous => previous + item) getOrElse Set(item)

    s.copy(
      creature = s.creature.copy(
        foundItems = s.creature.foundItems + (finder -> next),
      ),
      index = s.index.copy(
        itemFinders = ItemFinderIndexAddMutation(finder, item)(s.index.itemFinders)
      )
    )
  }
}
