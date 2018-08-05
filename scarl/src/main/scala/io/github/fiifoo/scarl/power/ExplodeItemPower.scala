package io.github.fiifoo.scarl.power

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Power.Resources
import io.github.fiifoo.scarl.core.entity.{ContainerId, CreatureId, ItemId, ItemPower}
import io.github.fiifoo.scarl.effect.combat.ExplodeEffect

case class ExplodeItemPower(description: Option[String] = None,
                            resources: Option[Resources] = None,
                           ) extends ItemPower {

  def apply(s: State, item: ItemId, user: Option[CreatureId]): List[Effect] = {
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
