package io.github.fiifoo.scarl.geometry

import io.github.fiifoo.scarl.core.Selectors.{getLocationEntities, getLocationItems}
import io.github.fiifoo.scarl.core.entity.{CreatureId, EntityId, ItemId, WallId}
import io.github.fiifoo.scarl.core.{Location, State}

object Obstacle {

  def movement(s: State)(location: Location): Option[EntityId] = {
    (getLocationEntities(s)(location) collectFirst {
      case creature: CreatureId => creature
      case wall: WallId => wall
    }) orElse {
      getClosedDoor(s)(location)
    }
  }

  def shot(s: State)(location: Location): Option[EntityId] = movement(s)(location)

  def sight(s: State)(location: Location): Option[EntityId] = {
    (getLocationEntities(s)(location) collectFirst {
      case wall: WallId => wall
    }) orElse {
      getClosedDoor(s)(location)
    }
  }

  private def getClosedDoor(s: State)(location: Location): Option[ItemId] = {
    getLocationItems(s)(location) find (_ (s).door.exists(!_.open))
  }
}
