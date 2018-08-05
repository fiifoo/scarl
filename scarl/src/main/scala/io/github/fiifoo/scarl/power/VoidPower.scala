package io.github.fiifoo.scarl.power

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Power.Resources
import io.github.fiifoo.scarl.core.entity._

case class VoidPower(description: Option[String] = None,
                     resources: Option[Resources] = None,
                    ) extends CreaturePower with ItemPower {
  override def apply(s: State, usable: UsableId, user: Option[CreatureId]): List[Effect] = {
    List()
  }

  def apply(s: State, creature: CreatureId, user: Option[CreatureId]): List[Effect] = {
    List()
  }

  def apply(s: State, item: ItemId, user: Option[CreatureId]): List[Effect] = {
    List()
  }
}
