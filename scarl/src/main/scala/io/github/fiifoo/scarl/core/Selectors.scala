package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.creature.Stats
import io.github.fiifoo.scarl.core.entity._

object Selectors {

  def getContainerItems(s: State)(container: EntityId): Set[ItemId] = {
    s.index.containerItems.getOrElse(container, Set())
  }

  def getCreatureStats(s: State)(creature: CreatureId): Stats = {
    creature(s).stats add getEquipmentStats(s)(creature)
  }

  def getEquipmentStats(s: State)(creature: CreatureId): Stats = {
    s.index.equipmentStats.getOrElse(creature, Stats())
  }

  def getLocationEntities(s: State)(location: Location): Set[LocatableId] = {
    s.index.locationEntities.getOrElse(location, Set())
  }

  def getLocationItems(s: State)(location: Location): Set[ItemId] = {
    s.index.locationEntities.get(location) map (entities => {
      (entities collect {
        case container: ContainerId => getContainerItems(s)(container)
      }).flatten
    }) getOrElse Set()
  }

  def getLocationTriggers(s: State)(location: Location): Set[TriggerStatusId] = {
    s.index.locationTriggers.getOrElse(location, Set())
  }

  def getTargetStatuses(s: State)(target: EntityId): Set[StatusId] = {
    s.index.targetStatuses.getOrElse(target, Set())
  }
}
