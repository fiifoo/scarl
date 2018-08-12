package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.geometry.Location

case class MachineryActivatedEffect(activator: CreatureId,
                                    location: Location,
                                    description: Option[String],
                                    parent: Option[Effect] = None
                                   ) extends Effect {

  def apply(s: State): EffectResult = EffectResult()
}
