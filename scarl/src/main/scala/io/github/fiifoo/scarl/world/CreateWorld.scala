package io.github.fiifoo.scarl.world

import io.github.fiifoo.scarl.area.{Area, AreaId}
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.core.world.ConduitId

object CreateWorld {

  def apply(assets: WorldAssets,
            firstArea: AreaId,
            player: CreatureKindId,
            rng: Rng = Rng(1)
           ): (WorldState, CreatureId) = {
    val initial = createConduits(WorldState(assets))
    val world = GenerateArea(initial, firstArea, rng)

    addPlayer(world, firstArea, player)
  }

  private def createConduits(world: WorldState): WorldState = {
    val (conduits, nextId) = (world.assets.areas.values foldLeft(List[Conduit](), world.nextConduitId)) ((carry, area) => {
      val (conduits, nextId) = carry
      val (added, newId) = createAreaConduits(area, nextId)

      (conduits ::: added, newId)
    })

    world.copy(
      conduits = (conduits map (x => (x.id, x))).toMap,
      nextConduitId = nextId
    )
  }

  private def createAreaConduits(area: Area, nextId: Int): (List[Conduit], Int) = {
    (area.conduits foldLeft(List[Conduit](), nextId)) ((carry, x) => {
      val (result, nextId) = carry

      val conduit = Conduit(ConduitId(nextId), area.id, x.target, x.sourceItem, x.targetItem, x.tag)

      (conduit :: result, nextId + 1)
    })
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
