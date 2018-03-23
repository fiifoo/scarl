package io.github.fiifoo.scarl.power

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, RemoveEntityEffect}
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId, Selectors}
import io.github.fiifoo.scarl.core.item.ItemPower

case class RemoveItemPower(description: Option[String] = None) extends ItemPower {

  def apply(s: State, item: ItemId, user: Option[CreatureId] = None): List[Effect] = {
    List(RemoveEntityEffect(
      target = item,
      location = Selectors.getItemLocation(s)(item),
      description = description
    ))
  }
}
