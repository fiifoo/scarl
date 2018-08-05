package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.{CreatureId, MachineryId}
import io.github.fiifoo.scarl.core.geometry.Location

case class ActivateMachineryEffect(activator: CreatureId,
                                   location: Location,
                                   machinery: MachineryId,
                                   parent: Option[Effect] = None
                                  ) extends Effect {

  def apply(s: State): EffectResult = {
    val mechanism = machinery(s).mechanism
    val usedEffect = MachineryActivatedEffect(activator, mechanism.description, Some(this))
    val effects = machinery(s).interact(s, location)

    EffectResult(usedEffect :: effects)
  }
}
