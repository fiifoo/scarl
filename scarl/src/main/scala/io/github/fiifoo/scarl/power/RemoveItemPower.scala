package io.github.fiifoo.scarl.power

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, RemoveEntityEffect}
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId, ItemPower, Selectors}

case class RemoveItemPower(removeDescription: Option[String] = None,
                           useDescription: Option[String] = None,
                          ) extends ItemPower {

  def apply(s: State, item: ItemId, user: Option[CreatureId]): List[Effect] = {
    List(RemoveEntityEffect(
      target = item,
      location = Selectors.getItemLocation(s)(item),
      description = removeDescription
    ))
  }
}
