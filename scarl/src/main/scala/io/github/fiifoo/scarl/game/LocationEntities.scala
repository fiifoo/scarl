package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.world.ConduitId
import io.github.fiifoo.scarl.core.{Location, Selectors, State}

object LocationEntities {
  def apply(s: State, location: Location): LocationEntities = {
    val entities = Selectors.getLocationEntities(s)(location)
    val items = Selectors.getLocationItems(s)(location) map (_ (s)) filterNot (_.hidden)

    LocationEntities(
      s.index.locationConduit.get(location),
      entities collectFirst { case c: CreatureId => c(s) },
      entities collectFirst { case t: TerrainId => t(s) },
      entities collectFirst { case w: WallId => w(s) },
      items
    )
  }
}

case class LocationEntities(conduit: Option[ConduitId],
                            creature: Option[Creature],
                            terrain: Option[Terrain],
                            wall: Option[Wall],
                            items: Set[Item]
                           )
