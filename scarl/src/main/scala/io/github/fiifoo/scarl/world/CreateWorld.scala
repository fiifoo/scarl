package io.github.fiifoo.scarl.world

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.core._
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.CreatureKindId

object CreateWorld {

  def apply(assets: WorldAssets,
            firstArea: AreaId,
            player: CreatureKindId,
            rng: Rng = Rng(1)
           ): (WorldState, CreatureId) = {

    val initial = WorldState(assets)
    val world = GenerateArea(initial, firstArea, rng)

    addPlayer(world, firstArea, player)
  }

  private def addPlayer(world: WorldState,
                        area: AreaId,
                        player: CreatureKindId
                       ): (WorldState, CreatureId) = {

    val state = world.states(area)
    if (state.gateways.isEmpty) {
      throw new Exception("First area does not contain any gateways. Cannot add player.")
    }

    val (location, _) = state.rng.nextChoice(state.gateways)
    val result = player(state).toLocation(state, state.idSeq, location)

    val nextState = result.write(state)
    val nextWorld = world.copy(
      states = world.states + (area -> nextState)
    )

    (nextWorld, result.entity.id)
  }
}
