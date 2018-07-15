package io.github.fiifoo.scarl.world

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.core._
import io.github.fiifoo.scarl.core.entity.IdSeq
import io.github.fiifoo.scarl.core.mutation.ConduitExitMutation
import io.github.fiifoo.scarl.core.world.{ConduitId, Traveler}

object ChangeArea {

  def apply(world: WorldState,
            currentArea: AreaId,
            currentState: State,
            conduit: ConduitId,
            traveler: Traveler
           ): (WorldState, AreaId) = {

    val currentWorld = world.copy(states = world.states + (currentArea -> currentState))
    val nextArea = getConduitExit(world.conduits(conduit), currentArea)
    val nextWorld = if (world.states.get(nextArea).isDefined) {
      reloadArea(currentWorld, nextArea, currentState.idSeq)
    } else {
      GenerateArea(currentWorld, nextArea, currentState.rng, currentState.idSeq)
    }
    val finalWorld = applyConduitExit(nextWorld, nextArea, conduit, traveler)

    (finalWorld, nextArea)
  }

  private def reloadArea(world: WorldState, area: AreaId, idSeq: IdSeq): WorldState = {
    val state = world.states(area).copy(idSeq = idSeq)

    world.copy(
      states = world.states + (area -> state)
    )
  }

  private def getConduitExit(conduit: Conduit, entry: AreaId): AreaId = {
    if (conduit.source == entry) {
      conduit.target
    } else {
      conduit.source
    }
  }

  private def applyConduitExit(world: WorldState,
                               area: AreaId,
                               conduit: ConduitId,
                               traveler: Traveler
                              ): WorldState = {
    val state = world.states(area)
    val location = state.conduits.exits(conduit)
    val nextState = ConduitExitMutation(traveler, location)(state)

    world.copy(
      states = world.states + (area -> nextState)
    )
  }
}
