package io.github.fiifoo.scarl.world

import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.core.world.ConduitId

object CreateWorld {

  def apply(assets: WorldAssets,
            world: World,
            player: CreatureKindId,
            rng: Rng = Rng(1)
           ): (WorldState, CreatureId) = {
    val initial = createConduits(world, WorldState(assets, transports = world.transports))
    val state = GenerateArea(initial, world.start, rng)

    addPlayer(state, world.start, player)
  }

  private def createConduits(world: World, state: WorldState): WorldState = {
    val (conduits, nextId) = (world.conduits foldLeft(List[Conduit](), state.nextConduitId)) ((carry, x) => {
      val (conduits, nextId) = carry
      val conduit = Conduit(ConduitId(nextId), x.source, x.target, x.sourceItem, x.targetItem, x.tag)

      (conduit :: conduits, nextId + 1)
    })

    state.copy(
      conduits = (conduits map (x => (x.id, x))).toMap,
      nextConduitId = nextId
    )
  }

  private def addPlayer(world: WorldState,
                        area: SiteId,
                        player: CreatureKindId
                       ): (WorldState, CreatureId) = {

    val state = world.states(area)
    if (state.gateways.isEmpty) {
      throw new Exception("First area does not contain any gateways. Cannot add player.")
    }

    val (location, _) = state.rng.nextChoice(state.gateways)
    val result = player(state).apply(state, state.idSeq, location)

    val nextState = result.write(state)
    val nextWorld = world.copy(
      states = world.states + (area -> nextState)
    )

    (nextWorld, result.entity.id)
  }
}
