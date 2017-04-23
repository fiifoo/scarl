package io.github.fiifoo.scarl.core.mutation.index

import io.github.fiifoo.scarl.core._
import io.github.fiifoo.scarl.core.entity._

case class NewEntityIndexMutation(entity: Entity) {

  def apply(s: State, index: State.Index): State.Index = {
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