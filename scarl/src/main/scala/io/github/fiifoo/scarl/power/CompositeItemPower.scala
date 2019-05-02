package io.github.fiifoo.scarl.power

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Power.Resources
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.effect.interact.PowerUseEffect

case class CompositeItemPower(description: Option[String] = None,
                              resources: Option[Resources] = None,
                              powers: List[ItemPower],
                             ) extends ItemPower {
  def apply(s: State, item: ItemId, user: Option[CreatureId]): List[Effect] = {
    this.powers map (PowerUseEffect(user, item, _, requireResources = false))
  }
}
