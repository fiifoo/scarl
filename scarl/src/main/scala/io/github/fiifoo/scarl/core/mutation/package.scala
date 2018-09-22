package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.entity.Selectors.getItemLocation
import io.github.fiifoo.scarl.core.entity.{Entity, Item, Wall}

package object mutation {

  def checkWaypointNetworkChange(s: State, entity: Entity): State = {
    entity match {
      case wall: Wall => s.copy(tmp = s.tmp.copy(
        waypointNetworkChanged = s.tmp.waypointNetworkChanged + wall.location
      ))
      case item: Item if item.isLockedDoor => s.copy(tmp = s.tmp.copy(
        waypointNetworkChanged = getItemLocation(s)(item.id) map (s.tmp.waypointNetworkChanged + _) getOrElse
          s.tmp.waypointNetworkChanged
      ))
      case _ => s
    }
  }
}
