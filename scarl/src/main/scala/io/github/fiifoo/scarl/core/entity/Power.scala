package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Power.Resources

object Power {

  case class Resources(health: Int = 0,
                       energy: Int = 0,
                       materials: Int = 0,
                       components: Int = 0,
                      )

}

sealed trait Power {
  val description: Option[String]
  val resources: Option[Resources]

  def apply(s: State, usable: UsableId, user: Option[CreatureId]): List[Effect]
}

trait CreaturePower extends Power {
  def apply(s: State, creature: CreatureId, user: Option[CreatureId]): List[Effect]

  def apply(s: State, usable: UsableId, user: Option[CreatureId]): List[Effect] = {
    usable match {
      case creature: CreatureId => this.apply(s, creature, user)
      case _ => List()
    }
  }
}

trait ItemPower extends Power {
  def apply(s: State, item: ItemId, user: Option[CreatureId]): List[Effect]

  def apply(s: State, usable: UsableId, user: Option[CreatureId]): List[Effect] = {
    usable match {
      case item: ItemId => this.apply(s, item, user)
      case _ => List()
    }
  }
}
