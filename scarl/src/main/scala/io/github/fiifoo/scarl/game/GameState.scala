package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.game.area.MapLocation
import io.github.fiifoo.scarl.game.player.Settings
import io.github.fiifoo.scarl.game.statistics.Statistics
import io.github.fiifoo.scarl.world.{SiteId, WorldState}

case class GameState(area: SiteId,
                     player: CreatureId,
                     world: WorldState,
                     maps: Map[SiteId, Map[Location, MapLocation]] = Map(),
                     settings: Settings = Settings(),
                     statistics: Statistics = Statistics()
                    )
