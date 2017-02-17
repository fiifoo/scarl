package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.entity._

object Selectors {

  def getContainerItems(s: State)(container: EntityId): List[ItemId] = {
    s.index.containerItems.getOrElse(container, List())
  }

  def getLocationEntities(s: State)(location: Location): List[LocatableId] = {
    s.index.locationEntities.getOrElse(location, List())
  }

  def getLocationTriggers(s: State)(location: Location): List[TriggerStatusId] = {
    s.index.locationTriggers.getOrElse(location, List())
  }

  def getTargetStatuses(s: State)(target: EntityId): List[StatusId] = {
    s.index.targetStatuses.getOrElse(target, List())
  }
}
