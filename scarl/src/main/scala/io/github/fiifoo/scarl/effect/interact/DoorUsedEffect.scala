package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import io.github.fiifoo.scarl.core.{Location, State}

case class DoorUsedEffect(user: Option[CreatureId],
                          door: ItemId,
                          opened: Boolean,
                          location: Location,
                          parent: Option[Effect] = None
                         ) extends Effect {

  def apply(s: State): EffectResult = EffectResult()
}
