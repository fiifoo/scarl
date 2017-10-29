package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.world.ConduitId
import io.github.fiifoo.scarl.core.{Location, Selectors, State}

object LocationEntities {
  def apply(s: State, location: Location): LocationEntities = {
    def removable(entity: EntityId) = s.tmp.removableEntities contains entity

    val entities = Selectors.getLocationEntities(s)(location) filterNot removable
    val items = Selectors.getLocationVisibleItems(s)(location)

    LocationEntities(
      s.index.locationConduit.get(location),
      entities collectFirst { case c: CreatureId => c(s) },
      entities collectFirst { case t: TerrainId => t(s) },
      entities collectFirst { case w: WallId => w(s) },
      items map (_ (s))
    )
  }
}

case class LocationEntities(conduit: Option[ConduitId],
                            creature: Option[Creature],
                            terrain: Option[Terrain],
                            wall: Option[Wall],
                            items: Set[Item]
                           )
