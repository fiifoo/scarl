package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.ai.Behavior
import io.github.fiifoo.scarl.core.communication.CommunicationId
import io.github.fiifoo.scarl.core.creature.{Character, Events, FactionId, Missile, Party, Resources, Stats}
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item.Equipment.Slot
import io.github.fiifoo.scarl.core.item.Recipe.RecipeId
import io.github.fiifoo.scarl.core.item.{Equipment, Lock}
import io.github.fiifoo.scarl.core.kind.Kind.{Options, Result}
import io.github.fiifoo.scarl.core.mutation._
import io.github.fiifoo.scarl.core.{Color, State}

case class CreatureKind(id: CreatureKindId,
                        name: String,
                        display: Char,
                        color: Color,
                        faction: FactionId,
                        solitary: Boolean = false,
                        behavior: Behavior,
                        stats: Stats,

                        character: Option[Character] = None,
                        events: Option[Events] = None,
                        flying: Boolean = false,
                        immobile: Boolean = false,
                        locked: Option[Lock.Source] = None,
                        missile: Option[Missile] = None,
                        usable: Option[CreaturePower] = None,

                        equipments: Map[Slot, ItemKindId] = Map(),
                        inventory: Set[ItemKindId] = Set(),
                        recipes: Set[RecipeId] = Set(),

                        greetings: Map[FactionId, List[CommunicationId]] = Map(),
                        responses: Map[FactionId, List[CommunicationId]] = Map()
                       ) extends Kind {

  def apply(s: State, idSeq: IdSeq, location: Location, options: Options = Options()): Result[Creature] = {
    val (nextId, creatureIdSeq) = idSeq()
    val creatureId = CreatureId(nextId)

    val creature = Creature(
      id = creatureId,
      kind = id,
      faction = options.owner map (_ (s).faction) getOrElse faction,
      solitary = solitary,
      party = getParty(s, location, creatureId),
      behavior = behavior,
      location = location,
      tick = s.tick,
      resources = Resources(
        stats.energy.max,
        stats.materials.max
      ),
      stats = stats,
      owner = options.owner map SafeCreatureId.apply,
      tags = options.tags,

      character = character,
      events = events,
      flying = flying,
      immobile = immobile,
      locked = locked map (Lock(_, Some(creatureId))),
      missile = missile,
      usable = usable
    )

    val (equipmentMutations, equipmentIdSeq) = processEquipments(s, creatureIdSeq, creatureId)
    val (inventoryMutations, nextIdSeq) = processInventory(s, equipmentIdSeq, creatureId)
    val recipesMutations = if (recipes.nonEmpty) List(CreatureRecipesMutation(creatureId, recipes)) else List()

    Result(
      mutations =
        List(IdSeqMutation(nextIdSeq), NewEntityMutation(creature)) :::
          equipmentMutations :::
          inventoryMutations :::
          recipesMutations,
      idSeq = nextIdSeq,
      entity = creature
    )
  }

  private def getParty(s: State, location: Location, self: CreatureId): Party = {
    if (solitary) {
      Party(self)
    } else {
      Party.find(s, this, location) getOrElse Party(self)
    }
  }

  private def processEquipments(s: State, idSeq: IdSeq, creature: CreatureId): (List[Mutation], IdSeq) = {
    (equipments foldLeft(List[Mutation](), idSeq)) ((carry, element) => {
      val (mutations, idSeq) = carry
      val (slot, item) = element
      val result = item(s).apply(s, idSeq, creature, Options())

      Equipment.selectEquipment(slot, result.entity) map (equipment => {
        val slots = if (equipment.fillAll) equipment.slots else Set(slot)

        (mutations ::: result.mutations ::: List(EquipItemMutation(creature, result.entity.id, slots)), result.idSeq)
      }) getOrElse {
        (mutations ::: result.mutations, result.idSeq)
      }
    })
  }

  private def processInventory(s: State, idSeq: IdSeq, creature: CreatureId): (List[Mutation], IdSeq) = {
    (inventory foldLeft(List[Mutation](), idSeq)) ((carry, item) => {
      val (mutations, idSeq) = carry
      val result = item(s).apply(s, idSeq, creature, Options())

      (mutations ::: result.mutations, result.idSeq)
    })
  }
}
