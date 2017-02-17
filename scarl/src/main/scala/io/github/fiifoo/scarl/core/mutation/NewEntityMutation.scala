package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core._
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.mutation.index._

case class NewEntityMutation(entity: Entity) extends Mutation {

  def apply(s: State): State = {
    if (entity.id.value != s.nextEntityId) {
      throw new Exception(s"${entity.id} does not match state next entity id")
    }

    val addedActors = entity match {
      case actor: Actor => actor.id :: s.tmp.addedActors
      case _ => s.tmp.addedActors
    }

    s.copy(
      entities = s.entities + (entity.id -> entity),
      index = mutateIndex(s, s.index, entity),
      nextEntityId = entity.id.value + 1,
      tmp = s.tmp.copy(addedActors = addedActors)
    )
  }

  private def mutateIndex(s: State, index: State.Index, entity: Entity): State.Index = {
    index.copy(
      containerItems = entity match {
        case item: Item => ItemContainerIndexAddMutation(item.id, item.container)(index.containerItems)
        case _ => index.containerItems
      },
      factionMembers = entity match {
        case member: Creature => FactionMemberIndexAddMutation(member.id, member.faction)(index.factionMembers)
        case _ => index.factionMembers
      },
      locationEntities = entity match {
        case locatable: Locatable => LocatableLocationIndexAddMutation(locatable.id, locatable.location)(index.locationEntities)
        case _ => index.locationEntities
      },
      locationTriggers = entity match {
        case trigger: TriggerStatus => TriggerLocationIndexAddMutation(trigger.id, trigger.target(s).location)(index.locationTriggers)
        case _ => index.locationTriggers
      },
      targetStatuses = entity match {
        case status: Status => StatusTargetIndexAddMutation(status.id, status.target)(index.targetStatuses)
        case _ => index.targetStatuses
      }
    )
  }
}
