package io.github.fiifoo.scarl.game.map

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.world.ConduitId

object LocationEntities {
  def apply(s: State, player: CreatureId, location: Location): LocationEntities = {
    def removable(entity: EntityId) = s.tmp.removableEntities contains entity

    val entities = Selectors.getLocationEntities(s)(location) filterNot removable
    val items = Selectors.getLocationVisibleItems(s, player)(location)

    LocationEntities(
      s.index.locationConduit.get(location),
      entities collect { case c: CreatureId => c(s) },
      entities collectFirst { case t: TerrainId => t(s) },
      entities collectFirst { case w: WallId => w(s) },
      items map (_ (s))
    )
  }
}

case class LocationEntities(conduit: Option[ConduitId],
                            creatures: Set[Creature],
                            terrain: Option[Terrain],
                            wall: Option[Wall],
                            items: Set[Item]
                           )