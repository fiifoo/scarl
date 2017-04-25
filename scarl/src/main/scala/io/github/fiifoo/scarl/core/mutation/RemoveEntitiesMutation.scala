package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State.Communications
import io.github.fiifoo.scarl.core._
import io.github.fiifoo.scarl.core.action.Tactic
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.equipment.Slot
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

    s.copy(
      communications = mutateCommunications(s.communications, removable),
      entities = s.entities -- removable,
      equipments = mutateEquipments(s.equipments, removable),
      index = mutateIndex(s, removable),
      tactics = mutateTactics(s.tactics, removable),
      tmp = s.tmp.copy(removableEntities = List())
    )
  }

  private def mutateCommunications(communications: Communications, removable: List[EntityId]): Communications = {
    communications.copy(
      received = communications.received -- collectCreatures(removable)
    )
  }

  private def mutateEquipments(equipments: Equipments, removable: List[EntityId]): Equipments = {
    equipments -- collectCreatures(removable)
  }

  private def mutateTactics(tactics: Tactics, removable: List[EntityId]): Tactics = {
    tactics -- collectCreatures(removable)
  }

  private def mutateIndex(s: State, removable: List[EntityId]): State.Index = {
    val index = removable.foldLeft(s.index)((index, entity) => {
      mutateSingleIndex(s, index, entity(s))
    })

    index.copy(
      equipmentStats = index.equipmentStats -- collectCreatures(removable)
    )
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
      targetStatuses = entity match {
        case status: Status => StatusTargetIndexRemoveMutation(status.id, status.target)(index.targetStatuses)
        case _ => index.targetStatuses
      }
    )
  }

  private def collectCreatures(removable: List[EntityId]): List[CreatureId] = {
    removable collect { case c: CreatureId => c }
  }
}
