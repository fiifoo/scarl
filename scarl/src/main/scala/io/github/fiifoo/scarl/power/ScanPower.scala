package io.github.fiifoo.scarl.power

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Power.Resources
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.effect.interact.ScanEffect

case class ScanPower(description: Option[String] = None,
                     resources: Option[Resources] = None,
                     noise: Int,
                    ) extends CreaturePower with ItemPower {
  override def apply(s: State, usable: UsableId, user: Option[CreatureId]): List[Effect] = {
    scan(s, user)
  }

  def apply(s: State, creature: CreatureId, user: Option[CreatureId]): List[Effect] = {
    scan(s, user)
  }

  def apply(s: State, item: ItemId, user: Option[CreatureId]): List[Effect] = {
    scan(s, user)
  }

  private def scan(s: State, user: Option[CreatureId]): List[Effect] = {
    user map (user => {
      List(ScanEffect(user, user(s).location, this.noise))
    }) getOrElse {
      List()
    }
  }
}
