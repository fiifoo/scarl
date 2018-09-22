package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.kind.ItemKindId

case class ItemCraftedEffect(craftsman: CreatureId,
                             item: ItemKindId,
                             parent: Option[Effect] = None
                            ) extends Effect {

  def apply(s: State): EffectResult = EffectResult()
}
