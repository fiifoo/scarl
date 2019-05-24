package io.github.fiifoo.scarl.power

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Power.Resources
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.item.{Key, SharedKey}
import io.github.fiifoo.scarl.effect.creature.ReceiveKeyEffect

case class ReceiveKeyPower(description: Option[String] = None,
                           resources: Option[Resources] = None,
                           key: Option[SharedKey],
                          ) extends CreaturePower with ItemPower {

  override def apply(s: State, usable: UsableId, user: Option[CreatureId]): List[Effect] = {
    usable match {
      case creature: CreatureId => this.apply(s, creature, user)
      case item: ItemId => this.apply(s, item, user)
    }
  }

  def apply(s: State, creature: CreatureId, user: Option[CreatureId]): List[Effect] = {
    this.key map this.receive(user) getOrElse Nil
  }

  def apply(s: State, item: ItemId, user: Option[CreatureId]): List[Effect] = {
    this.key orElse item(s).key map this.receive(user) getOrElse Nil
  }

  private def receive(user: Option[CreatureId])(key: Key): List[Effect] = {
    user map (user => {
      List(ReceiveKeyEffect(user, key))
    }) getOrElse {
      Nil
    }
  }
}
