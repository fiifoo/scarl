package io.github.fiifoo.scarl.power

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{CreateEntityEffect, Effect}
import io.github.fiifoo.scarl.core.entity.Power.Resources
import io.github.fiifoo.scarl.core.entity.Selectors.getEntityLocation
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.KindId

case class CreateEntityPower(description: Option[String] = None,
                             resources: Option[Resources] = None,
                             kind: KindId,
                             createDescription: Option[String] = None,
                            ) extends CreaturePower with ItemPower {
  override def apply(s: State, usable: UsableId, user: Option[CreatureId]): List[Effect] = {
    create(s, usable, user)
  }

  def apply(s: State, creature: CreatureId, user: Option[CreatureId]): List[Effect] = {
    create(s, creature, user)
  }

  def apply(s: State, item: ItemId, user: Option[CreatureId]): List[Effect] = {
    create(s, item, user)
  }

  private def create(s: State, source: EntityId, user: Option[CreatureId]): List[Effect] = {
    val effect = getEntityLocation(s)(source, deep = true) map (location => {
      CreateEntityEffect(
        kind = this.kind,
        location = location,
        owner = user,
        description = this.createDescription
      )
    })

    effect map (List(_)) getOrElse Nil
  }
}
