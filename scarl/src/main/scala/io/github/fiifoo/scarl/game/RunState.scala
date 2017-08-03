package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.game.api.OutMessage
import io.github.fiifoo.scarl.game.event.Event
import io.github.fiifoo.scarl.game.map.MapLocation

case class RunState(areaMap: Map[Location, MapLocation],
                    ended: Boolean = false,
                    events: List[Event] = List(),
                    fov: PlayerFov = PlayerFov(),
                    gameState: GameState,
                    instance: State,
                    outMessages: List[OutMessage] = List(),
                    playerInfo: PlayerInfo,
                    statistics: Statistics,
                    stopped: Boolean = false
                   )
