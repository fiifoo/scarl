package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.ai.Behavior
import io.github.fiifoo.scarl.core.communication.CommunicationId
import io.github.fiifoo.scarl.core.creature.{Character, FactionId, Party, Resources, Stats, Traits}
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item.Equipment.Slot
import io.github.fiifoo.scarl.core.item.Recipe.RecipeId
import io.github.fiifoo.scarl.core.item.{Equipment, Lock}
import io.github.fiifoo.scarl.core.kind.Kind.{Options, Result}
import io.github.fiifoo.scarl.core.mutation._
import io.github.fiifoo.scarl.core.{Color, State}

object CreatureKind {

  sealed trait Category

  case object DefaultCategory extends Category

  case object TurretCategory extends Category

}

case class CreatureKind(id: CreatureKindId,
                        name: String,
                        display: Char,
                        color: Color,
                        description: Option[String] = None,
                        faction: FactionId,
                        behavior: Behavior,
                        stats: Stats,

                        character: Option[Character] = None,
                        locked: Option[Lock.Source] = None,
                        usable: Option[CreaturePower] = None,
                        traits: Traits = Traits(),

                        equipments: Map[Slot, ItemKindId] = Map(),
                        inventory: List[ItemKindId] = List(),
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
      faction = options.owner map (_ (s).faction) orElse options.faction getOrElse faction,
      behavior = behavior,
      stats = stats,

      character = character,
      locked = locked map (Lock(_, Some(creatureId))),
      usable = usable,
      traits = traits,

      location = location,
      owner = options.owner map SafeCreatureId.apply,
      party = getParty(s, location, creatureId),
      resources = Resources(
        stats.energy.max,
        stats.materials.max
      ),
      tags = options.tags,
      tick = s.tick,
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
    if (traits.leader || traits.solitary) {
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
