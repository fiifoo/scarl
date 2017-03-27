package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.{Location, Selectors, State}

object LocationEntities {
  def apply(s: State, location: Location): LocationEntities = {
    val entities = Selectors.getLocationEntities(s)(location)

    val getContainerItems = Selectors.getContainerItems(s) _
    val containers = entities collect { case c: ContainerId => c }
    val items = (containers flatMap getContainerItems) map (_ (s))

    LocationEntities(
      entities collectFirst { case c: CreatureId => c(s) },
      entities collectFirst { case t: TerrainId => t(s) },
      entities collectFirst { case w: WallId => w(s) },
      items
    )
  }
}

case class LocationEntities(creature: Option[Creature],
                            terrain: Option[Terrain],
                            wall: Option[Wall],
                            items: Set[Item]
                           )
