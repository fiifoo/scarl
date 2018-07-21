package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, UsableId}
import io.github.fiifoo.scarl.core.geometry.Location

case class LockedUsableEffect(user: CreatureId,
                              usable: UsableId,
                              location: Location,
                              parent: Option[Effect] = None
                             ) extends Effect {

  def apply(s: State): EffectResult = EffectResult()
}
