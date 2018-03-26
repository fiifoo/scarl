package io.github.fiifoo.scarl.power

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.{ContainerId, CreatureId, ItemId}
import io.github.fiifoo.scarl.core.item.ItemPower
import io.github.fiifoo.scarl.effect.combat.ExplodeEffect

case object ExplodeItemPower extends ItemPower {

  def apply(s: State, item: ItemId, user: Option[CreatureId] = None): List[Effect] = {
    val container = item(s).container match {
      case container: ContainerId => Some(container)
      case _ => None
    }

    val effect = container flatMap (container => {
      item(s).explosive map (stats => {
        ExplodeEffect(
          container,
          container(s).location,
          stats
        )
      })
    })

    effect map (List(_)) getOrElse List()
  }
}
