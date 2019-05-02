package io.github.fiifoo.scarl.power

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Power.Resources
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.effect.MaybeEffect
import io.github.fiifoo.scarl.effect.interact.PowerUseEffect

case class MaybeItemPower(description: Option[String] = None,
                          resources: Option[Resources] = None,
                          power: ItemPower,
                          probability: Int,
                         ) extends ItemPower {
  def apply(s: State, item: ItemId, user: Option[CreatureId]): List[Effect] = {
    List(MaybeEffect(PowerUseEffect(user, item, this.power, requireResources = false), this.probability))
  }
}
