package io.github.fiifoo.scarl.mechanism

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, RemoveEntityEffect}
import io.github.fiifoo.scarl.core.entity.{Machinery, Wall}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item.Mechanism

case class RemoveWallMechanism(removeDescription: Option[String]) extends Mechanism {
  def interact(s: State, machinery: Machinery, control: Location): List[Effect] = {
    val walls = machinery.getTargetEntities(s) collect {
      case wall: Wall => wall
    }

    val effects = (walls map (wall => {
      RemoveEntityEffect(wall.id, Some(wall.location), removeDescription)
    })).toList

    activate(machinery, effects)
  }
}
