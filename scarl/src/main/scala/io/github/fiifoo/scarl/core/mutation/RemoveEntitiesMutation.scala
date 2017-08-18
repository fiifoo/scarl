package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core._
import io.github.fiifoo.scarl.core.action.Tactic
import io.github.fiifoo.scarl.core.entity._
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

    s.copy(
      cache = mutateCache(s, removable),
      receivedCommunications = s.receivedCommunications -- collectCreatures(removable),
      entities = s.entities -- removable,
      equipments = mutateEquipments(s.equipments, removable),
      index = mutateIndex(s, removable),
      simulation = simulation,
      tactics = mutateTactics(s.tactics, removable),
      tmp = s.tmp.copy(removableEntities = Set())
    )
  }

  private def mutateEquipments(equipments: Equipments, removable: Set[EntityId]): Equipments = {
    equipments -- collectCreatures(removable)
  }

  private def mutateTactics(tactics: Tactics, removable: Set[EntityId]): Tactics = {
    tactics -- collectCreatures(removable)
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
      locationEntities = entity match {
        case locatable: Locatable => LocatableLocationIndexRemoveMutation(locatable.id, locatable.location)(index.locationEntities)
        case _ => index.locationEntities
      },
      locationTriggers = entity match {
        case trigger: TriggerStatus => TriggerLocationIndexRemoveMutation(trigger.id, trigger.target(s).location)(index.locationTriggers)
        case _ => index.locationTriggers
      },
      partyMembers = entity match {
        case member: Creature => PartyMemberIndexRemoveMutation(member.id, member.party)(index.partyMembers)
        case _ => index.partyMembers
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
}
