package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.core.world.ConduitId
import io.github.fiifoo.scarl.world._

object GenerateGame {

  def apply(assets: WorldAssets,
            world: World,
            character: CreatureKindId,
            rng: Rng = Rng(1)
           ): GameState = {
    val create =
      createConduits(world) _ andThen
        GenerateArea(world.start, rng) andThen
        addPlayer(world.start, character)

    val (worldState, player) = create(WorldState(assets, transports = world.transports))

    GameState(world.start, player, worldState)
  }

  private def createConduits(world: World)(state: WorldState): WorldState = {
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

  private def addPlayer(area: SiteId, character: CreatureKindId)(world: WorldState): (WorldState, CreatureId) = {
    val state = world.states(area)
    if (state.gateways.isEmpty) {
      throw new Exception("First area does not contain any gateways. Cannot add player.")
    }

    val (location, _) = state.rng.nextChoice(state.gateways)
    val result = character(state).apply(state, state.idSeq, location)

    val nextState = result.write(state)
    val nextWorld = world.copy(
      states = world.states + (area -> nextState)
    )

    (nextWorld, result.entity.id)
  }
}
