package io.github.fiifoo.scarl.mechanism

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, RemoveEntityEffect}
import io.github.fiifoo.scarl.core.entity.Selectors.getLocationEntities
import io.github.fiifoo.scarl.core.entity.{Machinery, WallId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item.Mechanism

case class RemoveWallMechanism(disposable: Boolean,
                               removeDescription: Option[String],
                               activateDescription: Option[String],
                              ) extends Mechanism {
  def interact(s: State, machinery: Machinery, control: Location): List[Effect] = {
    val walls = machinery.targets flatMap getLocationEntities(s) collect {
      case wall: WallId => wall
    }
    val effects = (walls map (wall => {
      RemoveEntityEffect(wall, Some(wall(s).location), removeDescription)
    })).toList

    activate(machinery, effects)
  }
}
