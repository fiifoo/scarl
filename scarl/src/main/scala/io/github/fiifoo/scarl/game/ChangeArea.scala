package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.core._
import io.github.fiifoo.scarl.core.entity.IdSeq
import io.github.fiifoo.scarl.core.mutation.{ConduitExitMutation, ResetGoalsMutation}
import io.github.fiifoo.scarl.core.world.{ConduitId, Traveler}
import io.github.fiifoo.scarl.world.{Conduit, GenerateArea, SiteId, WorldState}

object ChangeArea {

  def apply(world: WorldState,
            currentArea: SiteId,
            currentState: State,
            conduit: ConduitId,
            traveler: Traveler
           ): (WorldState, SiteId) = {
    val currentWorld = world.copy(
      goals = world.goals ++ currentState.creature.goals,
      states = world.states + (currentArea -> ResetGoalsMutation()(currentState))
    )
    val nextArea = getConduitExit(world.conduits(conduit), currentArea)
    val nextWorld = if (world.states.get(nextArea).isDefined) {
      reloadArea(currentWorld, nextArea, currentState.idSeq)
    } else {
      GenerateArea(nextArea, currentState.rng, currentState.idSeq)(currentWorld)
    }
    val finalWorld = applyConduitExit(nextWorld, nextArea, conduit, traveler)

    (finalWorld, nextArea)
  }

  private def reloadArea(world: WorldState, area: SiteId, idSeq: IdSeq): WorldState = {
    val state = world.states(area).copy(idSeq = idSeq)

    world.copy(
      states = world.states + (area -> state)
    )
  }

  private def getConduitExit(conduit: Conduit, entry: SiteId): SiteId = {
    if (conduit.source == entry) {
      conduit.target
    } else {
      conduit.source
    }
  }

  private def applyConduitExit(world: WorldState,
                               area: SiteId,
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
