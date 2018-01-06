package io.github.fiifoo.scarl.game.map

import io.github.fiifoo.scarl.core.kind.{ItemKindId, TerrainKindId, WallKindId}

object MapLocation {
  def apply(entities: LocationEntities): MapLocation = {
    MapLocation(
      entities.terrain map (_.kind),
      entities.wall map (_.kind),
      entities.items map (_.kind)
    )
  }
}

case class MapLocation(terrain: Option[TerrainKindId],
                       wall: Option[WallKindId],
                       items: Set[ItemKindId]
                      )
