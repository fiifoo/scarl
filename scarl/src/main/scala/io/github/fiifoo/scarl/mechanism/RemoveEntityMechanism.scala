package io.github.fiifoo.scarl.mechanism

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, RemoveEntityEffect}
import io.github.fiifoo.scarl.core.entity.Machinery
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item.Mechanism

case class RemoveEntityMechanism(removeDescription: Option[String]) extends Mechanism {
  def interact(s: State, machinery: Machinery, control: Location): List[Effect] = {
    val entities = machinery.getTargetEntities(s)

    val effects = (entities map (entity => {
      RemoveEntityEffect(entity.id, Some(entity.location), removeDescription)
    })).toList

    activate(machinery, effects)
  }
}
