package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult, RemoveEntityEffect}
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import io.github.fiifoo.scarl.core.mutation.CreatureComponentsMutation

case class RecycleItemEffect(recycler: CreatureId,
                             item: ItemId,
                             parent: Option[Effect] = None
                            ) extends Effect {

  def apply(s: State): EffectResult = {
    item(s).recyclable map (components => {
      EffectResult(
        CreatureComponentsMutation(
          this.recycler,
          this.recycler(s).resources.components + components
        ),
        RemoveEntityEffect(item, parent = Some(this))
      )
    }) getOrElse {
      EffectResult()
    }
  }
}
