package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.game.map.MapLocation
import io.github.fiifoo.scarl.world.WorldState

case class GameState(area: AreaId,
                     maps: Map[AreaId, Map[Location, MapLocation]],
                     player: CreatureId,
                     world: WorldState
                    )
