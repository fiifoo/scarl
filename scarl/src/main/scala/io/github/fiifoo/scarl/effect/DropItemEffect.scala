package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{Container, ContainerId, CreatureId, ItemId}
import io.github.fiifoo.scarl.core.mutation.{IdSeqMutation, ItemContainerMutation, NewEntityMutation}

case class DropItemEffect(target: ItemId,
                          dropper: CreatureId,
                          parent: Option[Effect] = None
                         ) extends Effect {

  def apply(s: State): EffectResult = {
    val (nextId, nextIdSeq) = s.idSeq()
    val container = Container(ContainerId(nextId), dropper(s).location)

    EffectResult(List(
      IdSeqMutation(nextIdSeq),
      NewEntityMutation(container),
      ItemContainerMutation(target, container.id)
    ))
  }
}
