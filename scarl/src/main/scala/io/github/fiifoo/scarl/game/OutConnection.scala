package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.core.entity.Creature
import io.github.fiifoo.scarl.core.kind.Kinds
import io.github.fiifoo.scarl.core.{Location, State}

class OutConnection(send: OutMessage => Unit) {

  def apply(gameState: GameState,
            state: State,
            fov: Set[Location],
            messages: List[String],
            kinds: Option[Kinds] = None,
            statistics: Option[Statistics] = None
           ): Unit = {

    val message = OutMessage(
      gameState.area,
      calculateMessageFov(gameState, state, fov),
      messages,
      state.entities.get(gameState.player) map (_.asInstanceOf[Creature]),
      kinds,
      statistics
    )

    send(message)
  }

  private var previousArea: Option[AreaId] = None
  private var previousMessageFov: Map[Location, LocationEntities] = Map()

  private def calculateMessageFov(gameState: GameState,
                                  state: State,
                                  fov: Set[Location]): OutMessage.Fov = {

    val previous: Map[Location, LocationEntities] =
      if (previousArea.contains(gameState.area)) previousMessageFov else Map()

    val next = (fov map { location => location -> LocationEntities(state, location) }).toMap
    val delta = next filterNot (n => previous.contains(n._1) && previous(n._1) == n._2)
    val shouldHide = (previous -- next.keys filter (_._2.creature.isDefined)).keys

    previousArea = Some(gameState.area)
    previousMessageFov = next

    OutMessage.Fov(delta, shouldHide)
  }
}
