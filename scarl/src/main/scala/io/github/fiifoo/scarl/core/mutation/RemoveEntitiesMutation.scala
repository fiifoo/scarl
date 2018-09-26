package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core._
import io.github.fiifoo.scarl.core.ai.{Brain, Tactic}
import io.github.fiifoo.scarl.core.creature.FactionId
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.geometry.Sector
import io.github.fiifoo.scarl.core.item.Equipment.Slot
import io.github.fiifoo.scarl.core.mutation.index._

/**
  * Clears references to removed entities
  *
  * Warning: removing equipped item directly without unequipping it first is not supported
  */
case class RemoveEntitiesMutation() extends Mutation {
  type Tactics = Map[CreatureId, Tactic]
  type Equipments = Map[CreatureId, Map[Slot, ItemId]]

  def apply(s: State): State = {
    val removable = s.tmp.removableEntities

    val simulation = if (s.simulation.running) {
      s.simulation.copy(entities = s.simulation.entities -- removable)
    } else {
      s.simulation
    }

    val creatures = collectCreatures(removable)

    s.copy(
      brains = mutateBrains(s.brains, creatures),
      cache = mutateCache(s, removable),
      creature = s.creature.copy(
        recycledItems = s.creature.recycledItems -- creatures
      ),
      foundItems = mutateFoundItems(s, removable),
      receivedCommunications = s.receivedCommunications -- creatures,
      entities = s.entities -- removable,
      equipments = s.equipments -- creatures,
      index = mutateIndex(s, removable),
      keys = s.keys -- creatures,
      recipes = s.recipes -- creatures,
      simulation = simulation,
      tactics = s.tactics -- creatures,
      tmp = s.tmp.copy(removableEntities = Set())
    )
  }

  private def mutateBrains(brains: Map[FactionId, Brain], creatures: Set[CreatureId]): Map[FactionId, Brain] = {
    if (creatures.isEmpty) {
      brains
    } else {
      brains mapValues (brain => brain.copy(
        intentions = brain.intentions -- creatures
      ))
    }
  }

  private def mutateFoundItems(s: State, removable: Set[EntityId]): Map[CreatureId, Set[ItemId]] = {
    (collectItems(removable) foldLeft s.foundItems) ((found, item) => {
      (s.index.itemFinders.get(item) map (finders => {
        (finders foldLeft found) ((found, finder) => {
          val next = found(finder) - item

          if (next.isEmpty) {
            found - finder
          } else {
            found + (finder -> next)
          }
        })
      })) getOrElse found
    }) -- collectCreatures(removable)
  }

  private def mutateCache(s: State, removable: Set[EntityId]): State.Cache = {
    val cache = removable.foldLeft(s.cache)((cache, entity) => {
      mutateSingleCache(s, cache, entity(s))
    })

    cache.copy(
      equipmentStats = cache.equipmentStats -- collectCreatures(removable)
    )
  }

  private def mutateSingleCache(s: State, cache: State.Cache, entity: Entity): State.Cache = {
    cache.copy(
      actorQueue = entity match {
        case actor: Actor => cache.actorQueue.remove(actor)
        case _ => cache.actorQueue
      },
    )
  }

  private def mutateIndex(s: State, removable: Set[EntityId]): State.Index = {
    removable.foldLeft(s.index)((index, entity) => {
      mutateSingleIndex(s, index, entity(s))
    })
  }

  private def mutateSingleIndex(s: State, index: State.Index, entity: Entity): State.Index = {
    index.copy(
      containerItems = entity match {
        case item: Item => ItemContainerIndexRemoveMutation(item.id, item.container)(index.containerItems)
        case _ => index.containerItems
      },
      factionMembers = entity match {
        case member: Creature => FactionMemberIndexRemoveMutation(member.id, member.faction)(index.factionMembers)
        case _ => index.factionMembers
      },
      itemFinders = entity match {
        case creature: Creature => s.foundItems.get(creature.id) map (items => {
          ItemFinderIndexRemoveMutation(creature.id, items)(index.itemFinders)
        }) getOrElse index.itemFinders
        case item: Item => index.itemFinders - item.id
        case _ => index.itemFinders
      },
      locationEntities = entity match {
        case locatable: Locatable => LocatableLocationIndexRemoveMutation(locatable.id, locatable.location)(index.locationEntities)
        case _ => index.locationEntities
      },
      locationMachinery = entity match {
        case machinery: Machinery => LocationMachineryIndexRemoveMutation(machinery.id, machinery.controls)(index.locationMachinery)
        case _ => index.locationMachinery
      },
      locationTriggers = entity match {
        case trigger: TriggerStatus => TriggerLocationIndexRemoveMutation(trigger.id, trigger.target(s).location)(index.locationTriggers)
        case _ => index.locationTriggers
      },
      partyMembers = entity match {
        case member: Creature => PartyMemberIndexRemoveMutation(member.id, member.party)(index.partyMembers)
        case _ => index.partyMembers
      },
      sectorCreatures = entity match {
        case creature: Creature => CreatureSectorIndexRemoveMutation(creature.id, Sector(s)(creature.location))(index.sectorCreatures)
        case _ => index.sectorCreatures
      },
      sectorItems = entity match {
        case item: Item => item.container match {
          case container: ContainerId => ItemSectorIndexRemoveMutation(item.id, Sector(s)(container(s).location))(index.sectorItems)
          case _ => index.sectorItems
        }
        case _ => index.sectorItems
      },
      targetStatuses = entity match {
        case status: Status => StatusTargetIndexRemoveMutation(status.id, status.target)(index.targetStatuses)
        case _ => index.targetStatuses
      }
    )
  }

  private def collectCreatures(removable: Set[EntityId]): Set[CreatureId] = {
    removable collect { case c: CreatureId => c }
  }

  private def collectItems(removable: Set[EntityId]): Set[ItemId] = {
    removable collect { case i: ItemId => i }
  }
}
