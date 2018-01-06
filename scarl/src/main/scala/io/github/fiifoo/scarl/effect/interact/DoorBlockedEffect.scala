package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import io.github.fiifoo.scarl.core.geometry.Location

case class DoorBlockedEffect(user: Option[CreatureId],
                             door: ItemId,
                             obstacle: CreatureId,
                             location: Location,
                             parent: Option[Effect] = None
                            ) extends Effect {

  def apply(s: State): EffectResult = EffectResult()
}
