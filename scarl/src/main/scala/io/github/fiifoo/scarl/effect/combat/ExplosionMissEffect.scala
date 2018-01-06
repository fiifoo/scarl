package io.github.fiifoo.scarl.effect.combat

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, LocatableId}
import io.github.fiifoo.scarl.core.geometry.Location

case class ExplosionMissEffect(source: LocatableId,
                               target: CreatureId,
                               location: Location,
                               parent: Option[Effect] = None
                              ) extends Effect {

  def apply(s: State): EffectResult = EffectResult()
}
