package io.github.fiifoo.scarl.effect.interact

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult, LocalizedDescriptionEffect}
import io.github.fiifoo.scarl.core.entity.{CreatureId, MachineryId}
import io.github.fiifoo.scarl.core.geometry.Location

case class ActivateMachineryEffect(activator: CreatureId,
                                   location: Location,
                                   machinery: MachineryId,
                                   description: Option[String] = None,
                                   parent: Option[Effect] = None
                                  ) extends Effect with LocalizedDescriptionEffect {

  def apply(s: State): EffectResult = {
    EffectResult(machinery(s).interact(s, location))
  }
}
