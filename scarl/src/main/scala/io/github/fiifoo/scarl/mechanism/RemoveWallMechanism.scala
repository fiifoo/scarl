package io.github.fiifoo.scarl.mechanism

import io.github.fiifoo.scarl.core.Selectors.getLocationEntities
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.{Machinery, WallId}
import io.github.fiifoo.scarl.core.item.Mechanism
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.effect.area.RemoveEntityEffect

case class RemoveWallMechanism(disposable: Boolean, description: Option[String]) extends Mechanism {
  def interact(s: State, machinery: Machinery, control: Location): List[Effect] = {
    val walls = machinery.targets flatMap getLocationEntities(s) collect {
      case wall: WallId => wall
    }
    val effects = (walls map (wall => {
      RemoveEntityEffect(wall, Some(wall(s).location), description)
    })).toList

    activate(machinery, effects)
  }
}
