package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect

sealed trait Power {
  val useDescription: Option[String]

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
