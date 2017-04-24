package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State.Communications
import io.github.fiifoo.scarl.core._
import io.github.fiifoo.scarl.core.action.Tactic
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.mutation.index._

case class RemoveEntitiesMutation() extends Mutation {
  type Tactics = Map[CreatureId, Tactic]

  def apply(s: State): State = {
    val removable = s.tmp.removableEntities

    val index = removable.foldLeft(s.index)((index, entity) => {
      mutateIndex(s, index, entity(s))
    })

    s.copy(
      communications = mutateCommunications(s.communications, removable),
      entities = s.entities -- removable,
      index = index,
      tactics = mutateTactics(s.tactics, removable),
      tmp = s.tmp.copy(removableEntities = List())
    )
  }

  private def mutateCommunications(communications: Communications, removable: List[EntityId]): Communications = {
    val creatures = removable collect { case c: CreatureId => c }

    communications.copy(
      received = communications.received -- creatures
    )
  }

  private def mutateTactics(tactics: Tactics, removable: List[EntityId]): Tactics = {
    val creatures = removable collect { case c: CreatureId => c }

    tactics -- creatures
  }

  private def mutateIndex(s: State, index: State.Index, entity: Entity): State.Index = {
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
}
