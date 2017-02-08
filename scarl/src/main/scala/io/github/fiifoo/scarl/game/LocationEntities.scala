package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.{Location, Selectors, State}

object LocationEntities {
  def apply(s: State, location: Location): LocationEntities = {
    val entities = Selectors.getLocationEntities(s)(location)

    LocationEntities(
      entities collectFirst { case c: CreatureId => c(s) },
      entities collectFirst { case t: TerrainId => t(s) },
      entities collectFirst { case w: WallId => w(s) }
    )
  }
}

case class LocationEntities(creature: Option[Creature],
                            terrain: Option[Terrain],
                            wall: Option[Wall]
                           )
