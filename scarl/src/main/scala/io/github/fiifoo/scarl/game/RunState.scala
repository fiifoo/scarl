package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.game.api.{AreaChange, GameUpdate, OutMessage}
import io.github.fiifoo.scarl.game.area.MapLocation
import io.github.fiifoo.scarl.game.event.Event
import io.github.fiifoo.scarl.game.player.PlayerFov
import io.github.fiifoo.scarl.game.statistics.Statistics

case class RunState(areaMap: Map[Location, MapLocation],
                    brains: Option[CalculateBrains.Calculation] = None,
                    ended: Boolean = false,
                    events: List[Event] = List(),
                    fov: PlayerFov = PlayerFov(),
                    game: GameState,
                    instance: State,
                    outMessages: List[OutMessage] = List(),
                    paused: Boolean = false,
                    statistics: Statistics,
                    stopped: Boolean = false,

                    previous: Option[RunState] = None,
                   ) {
  def addMessage(message: OutMessage): RunState = {
    val messages = message :: this.outMessages

    message match {
      case _: AreaChange => this.copy(
        previous = None,
        outMessages = messages,
      )
      case _: GameUpdate => this.copy(
        events = Nil,
        previous = Some(this.copy(previous = None)),
        outMessages = messages,
      )
      case _ => this.copy(
        outMessages = messages,
      )
    }
  }
}
