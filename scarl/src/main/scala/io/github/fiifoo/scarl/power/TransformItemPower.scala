package io.github.fiifoo.scarl.power

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId, Locatable}
import io.github.fiifoo.scarl.core.kind.KindId
import io.github.fiifoo.scarl.core.power.{ItemPower, ItemPowerId}
import io.github.fiifoo.scarl.effect.TransformEffect

case class TransformItemPower[T <: Locatable](id: ItemPowerId,
                                              to: KindId[T],
                                              description: Option[String] = None
                                             ) extends ItemPower {

  def apply(s: State, item: ItemId, user: Option[CreatureId] = None): List[Effect] = {
    List(TransformEffect(
      from = item,
      to = to,
      owner = user,
      description = description
    ))
  }
}
