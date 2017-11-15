package io.github.fiifoo.scarl.status

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.world.ConduitId
import io.github.fiifoo.scarl.effect.interact.ConduitEntryEffect

case class TriggeredConduitStatus(id: TriggerStatusId,
                                  target: ContainerId,
                                  conduit: ConduitId
                                 ) extends TriggerStatus {

  def apply(s: State, triggerer: CreatureId): List[Effect] = {
    List(ConduitEntryEffect(triggerer, conduit))
  }
}
