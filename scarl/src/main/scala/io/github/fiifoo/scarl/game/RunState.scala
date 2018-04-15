package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.game.api.OutMessage
import io.github.fiifoo.scarl.game.area.MapLocation
import io.github.fiifoo.scarl.game.event.Event
import io.github.fiifoo.scarl.game.player.{PlayerFov, PlayerInfo}
import io.github.fiifoo.scarl.game.statistics.Statistics

case class RunState(areaMap: Map[Location, MapLocation],
                    ended: Boolean = false,
                    events: List[Event] = List(),
                    fov: PlayerFov = PlayerFov(),
                    gameState: GameState,
                    instance: State,
                    outMessages: List[OutMessage] = List(),
                    paused: Boolean = false,
                    playerInfo: PlayerInfo,
                    statistics: Statistics,
                    stopped: Boolean = false
                   )
