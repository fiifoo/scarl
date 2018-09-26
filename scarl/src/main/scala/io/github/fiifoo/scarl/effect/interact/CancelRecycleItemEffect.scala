package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.kind.ItemKindId
import io.github.fiifoo.scarl.core.kind.Kind.Options
import io.github.fiifoo.scarl.core.mutation.{CreatureComponentsMutation, ItemRecycleCanceledMutation}
import io.github.fiifoo.scarl.effect.creature.ShortageEffect

case class CancelRecycleItemEffect(recycler: CreatureId,
                                   item: ItemKindId,
                                   parent: Option[Effect] = None
                                  ) extends Effect {

  def apply(s: State): EffectResult = {
    item(s).recyclable map (components => {
      val resources = this.recycler(s).resources

      if (resources.components < components) {
        EffectResult(
          ShortageEffect(this.recycler, components = true, parent = Some(this))
        )
      } else {
        val itemMutations = this.item(s).apply(s, s.idSeq, this.recycler, Options()).mutations

        EffectResult(
          CreatureComponentsMutation(
            this.recycler,
            resources.components - components
          ) ::
            ItemRecycleCanceledMutation(
              this.item,
              this.recycler
            ) :: itemMutations
        )
      }
    }) getOrElse {
      EffectResult()
    }
  }
}
