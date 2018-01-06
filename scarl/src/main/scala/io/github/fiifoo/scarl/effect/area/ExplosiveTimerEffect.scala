package io.github.fiifoo.scarl.effect.area

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.ContainerId
import io.github.fiifoo.scarl.core.geometry.Location

case class ExplosiveTimerEffect(explosive: ContainerId,
                                location: Location,
                                timer: Int,
                                parent: Option[Effect] = None
                               ) extends Effect {

  def apply(s: State): EffectResult = EffectResult()
}
