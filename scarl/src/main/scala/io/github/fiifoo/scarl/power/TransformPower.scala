package io.github.fiifoo.scarl.power

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Power.Resources
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.KindId
import io.github.fiifoo.scarl.effect.area.TransformEffect

case class TransformPower(description: Option[String] = None,
                          resources: Option[Resources] = None,
                          transformTo: KindId,
                          transformDescription: Option[String] = None,
                         ) extends CreaturePower with ItemPower {
  override def apply(s: State, usable: UsableId, user: Option[CreatureId]): List[Effect] = {
    transform(s, usable, user)
  }

  def apply(s: State, creature: CreatureId, user: Option[CreatureId]): List[Effect] = {
    transform(s, creature, user)
  }

  def apply(s: State, item: ItemId, user: Option[CreatureId]): List[Effect] = {
    transform(s, item, user)
  }

  private def transform(s: State, from: EntityId, user: Option[CreatureId]): List[Effect] = {
    List(TransformEffect(
      from = from,
      to = transformTo,
      owner = user,
      description = transformDescription
    ))
  }
}
