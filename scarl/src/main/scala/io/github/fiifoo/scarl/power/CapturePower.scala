package io.github.fiifoo.scarl.power

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Power.Resources
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.effect.creature.CaptureEffect

case class CapturePower(description: Option[String] = None,
                        resources: Option[Resources] = None,
                       ) extends CreaturePower {

  def apply(s: State, creature: CreatureId, user: Option[CreatureId]): List[Effect] = {
    user map (user => {
      List(CaptureEffect(creature, user, creature(s).location))
    }) getOrElse {
      Nil
    }
  }
}
