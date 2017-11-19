package io.github.fiifoo.scarl.effect.combat

import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{ContainerId, CreatureId}
import io.github.fiifoo.scarl.core.{Location, State}

case class TrapMissEffect(trap: ContainerId,
                          target: CreatureId,
                          location: Location,
                          description: Option[String] = None,
                          parent: Option[Effect] = None
                         ) extends Effect {

  def apply(s: State): EffectResult = EffectResult()
}
