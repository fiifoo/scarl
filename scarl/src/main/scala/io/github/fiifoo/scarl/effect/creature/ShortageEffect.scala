package io.github.fiifoo.scarl.effect.creature

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId

case class ShortageEffect(target: CreatureId,
                          energy: Boolean = false,
                          materials: Boolean = false,
                          components: Boolean = false,
                          parent: Option[Effect] = None
                         ) extends Effect {

  def apply(s: State): EffectResult = EffectResult()
}
