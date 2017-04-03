package io.github.fiifoo.scarl.world

import io.github.fiifoo.scarl.area.template.{ApplyTemplate, CalculateTemplate, Template, TemplateId}
import io.github.fiifoo.scarl.area.{Area, AreaId, Conduit}
import io.github.fiifoo.scarl.core._
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.{CreatureKindId, Kinds}
import io.github.fiifoo.scarl.core.mutation.{NewEntityMutation, NewFactionMutation}

class WorldManager(val areas: Map[AreaId, Area],
                   val factions: Map[FactionId, Faction],
                   val kinds: Kinds,
                   val templates: Map[TemplateId, Template]
                  ) {

  def create(firstArea: AreaId, player: CreatureKindId, rng: Rng = Rng(1)): (WorldState, CreatureId) = {
    val initial = WorldState(areas)
    val world = generateArea(initial, firstArea, rng)

    addPlayer(world, firstArea, player)
  }

  def switchArea(world: WorldState,
                 currentArea: AreaId,
                 currentState: State,
                 conduit: ConduitId,
                 player: Creature
                ): (WorldState, AreaId, CreatureId) = {

    val currentWorld = world.copy(states = world.states + (currentArea -> currentState))
    val nextArea = getConduitExit(world.conduits(conduit), currentArea)
    val nextWorld = if (world.states.get(nextArea).isDefined) {
      currentWorld
    } else {
      generateArea(currentWorld, nextArea, currentState.rng)
    }

    addConduitTraveler(nextWorld, nextArea, conduit, player)
  }

  private def generateArea(world: WorldState,
                           area: AreaId,
                           rng: Rng
                          ): WorldState = {

    val s0 = State(kinds = kinds, rng = rng)
    val s1 = factions.values.foldLeft(s0)((s, faction) => {
      NewFactionMutation(faction)(s)
    })

    val (random, _) = s1.rng()
    val template = templates(areas(area).template)
    val templateResult = CalculateTemplate(template, templates, random)

    val in = (world.conduits.values filter (_.target == area)).toList
    val (out, nextConduitId) = createConduits(areas(area), world.nextConduitId)

    val state = ApplyTemplate(s1, templateResult, in, out, random)

    world.copy(
      states = world.states + (area -> state),
      conduits = world.conduits ++ (out map (c => (c.id, c))).toMap,
      nextConduitId = nextConduitId
    )
  }

  private def createConduits(area: Area, nextId: Int): (List[Conduit], Int) = {
    val fold = area.conduits.foldLeft[(List[Conduit], Int)](List(), nextId) _

    fold((carry, x) => {
      val (result, nextId) = carry
      val (target, sourceItem, targetItem) = x

      val conduit = Conduit(ConduitId(nextId), area.id, target, sourceItem, targetItem)

      (conduit :: result, nextId + 1)
    })
  }

  private def getConduitExit(conduit: Conduit, entry: AreaId): AreaId = {
    if (conduit.source == entry) {
      conduit.target
    } else {
      conduit.source
    }
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
    val creature = player(state)(state, location)

    val nextState = NewEntityMutation(creature)(state)
    val nextWorld = world.copy(
      states = world.states + (area -> nextState)
    )

    (
      nextWorld,
      creature.id
    )
  }

  private def addConduitTraveler(world: WorldState,
                                 area: AreaId,
                                 conduit: ConduitId,
                                 traveler: Creature
                                ): (WorldState, AreaId, CreatureId) = {
    val state = world.states(area)
    val location = state.conduits(conduit)
    val travelerId = CreatureId(state.nextEntityId)

    val nextState = NewEntityMutation(traveler.copy(
      id = CreatureId(state.nextEntityId),
      location = location,
      tick = state.tick
    ))(state)

    val nextWorld = world.copy(
      states = world.states + (area -> nextState)
    )

    (nextWorld, area, travelerId)
  }
}
