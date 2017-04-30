package io.github.fiifoo.scarl.world

import io.github.fiifoo.scarl.area.template.{ApplyTemplate, CalculateTemplate, Template, TemplateId}
import io.github.fiifoo.scarl.area.{Area, AreaId}
import io.github.fiifoo.scarl.core.State.Communications
import io.github.fiifoo.scarl.core._
import io.github.fiifoo.scarl.core.character.{Progression, ProgressionId}
import io.github.fiifoo.scarl.core.communication.{Communication, CommunicationId}
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.{CreatureKindId, Kinds}
import io.github.fiifoo.scarl.core.mutation.{ConduitExitMutation, NewEntityMutation, NewFactionMutation}
import io.github.fiifoo.scarl.core.world.{ConduitId, Traveler}

class WorldManager(val areas: Map[AreaId, Area],
                   val communications: Map[CommunicationId, Communication],
                   val factions: Map[FactionId, Faction],
                   val kinds: Kinds,
                   val progressions: Map[ProgressionId, Progression],
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
                 traveler: Traveler
                ): (WorldState, AreaId) = {

    val currentWorld = world.copy(states = world.states + (currentArea -> currentState))
    val nextArea = getConduitExit(world.conduits(conduit), currentArea)
    val nextWorld = if (world.states.get(nextArea).isDefined) {
      reloadArea(currentWorld, nextArea, currentState.nextEntityId)
    } else {
      generateArea(currentWorld, nextArea, currentState.rng, currentState.nextEntityId)
    }
    val finalWorld = applyConduitExit(nextWorld, nextArea, conduit, traveler)

    (finalWorld, nextArea)
  }

  private def generateArea(world: WorldState,
                           area: AreaId,
                           rng: Rng,
                           nextEntityId: Int = 1
                          ): WorldState = {

    val s0 = State(
      communications = Communications(communications),
      kinds = kinds,
      nextEntityId = nextEntityId,
      progressions = progressions,
      rng = rng
    )
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

  private def reloadArea(world: WorldState, area: AreaId, nextEntityId: Int): WorldState = {
    val state = world.states(area).copy(nextEntityId = nextEntityId)

    world.copy(
      states = world.states + (area -> state)
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

    (nextWorld, creature.id)
  }

  private def applyConduitExit(world: WorldState,
                               area: AreaId,
                               conduit: ConduitId,
                               traveler: Traveler
                              ): WorldState = {
    val state = world.states(area)
    val location = state.conduits(conduit)
    val nextState = ConduitExitMutation(traveler, location)(state)

    world.copy(
      states = world.states + (area -> nextState)
    )
  }
}
