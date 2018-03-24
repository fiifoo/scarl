package io.github.fiifoo.scarl.core.geometry

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.Selectors.{getLocationEntities, getLocationItems}
import io.github.fiifoo.scarl.core.entity.{CreatureId, EntityId, ItemId, WallId}
import io.github.fiifoo.scarl.core.item.Key

object Obstacle {

  def has(obstacle: Location => Option[EntityId])(location: Location): Boolean = {
    obstacle(location).isDefined
  }

  def explosion(s: State)(location: Location): Option[EntityId] = sight(s)(location)

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

  def travel(s: State, keys: Set[Key] = Set())(location: Location): Option[EntityId] = {
    (getLocationEntities(s)(location) collectFirst {
      case wall: WallId => wall
    }) orElse {
      getLockedDoor(s, keys)(location)
    }
  }

  def getClosedDoor(s: State)(location: Location): Option[ItemId] = {
    getLocationItems(s)(location) find (_ (s).door.exists(!_.open))
  }

  def getLockedDoor(s: State, keys: Set[Key] = Set())(location: Location): Option[ItemId] = {
    getLocationItems(s)(location) find (id => {
      val item = id(s)

      item.door exists (door => {
        !door.open && (item.locked exists (lock => {
          lock.key forall (key => !(keys contains key))
        }))
      })
    })
  }
}
