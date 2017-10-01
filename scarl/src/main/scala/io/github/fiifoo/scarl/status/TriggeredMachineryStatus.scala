package io.github.fiifoo.scarl.status

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity._

case class TriggeredMachineryStatus(id: TriggerStatusId,
                                    target: ContainerId
                                   ) extends TriggerStatus {

  def apply(s: State, triggerer: CreatureId): List[Effect] = {
    val location = target(s).location
    val machinery = s.index.locationMachinery.getOrElse(location, Set())

    machinery.toList flatMap (_ (s).interact(s, location))
  }
}
