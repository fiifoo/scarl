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

  def getEntityLocation(s: State)(entity: EntityId, deep: Boolean = false): Option[Location] = {
    entity match {
      case locatable: LocatableId => Some(locatable(s).location)
      case item: ItemId => getItemLocation(s)(item, deep)
      case status: StatusId => getStatusLocation(s)(status, deep)
      case _ => None
    }
  }

  def getEquipmentStats(s: State)(creature: CreatureId): Stats = {
    s.index.equipmentStats.getOrElse(creature, Stats())
  }

  def getItemLocation(s: State)(item: ItemId, deep: Boolean = false): Option[Location] = {
    item(s).container match {
      case container: ContainerId => Some(container(s).location)
      case container: EntityId if deep => getEntityLocation(s)(container, deep)
      case _ => None
    }
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

  def getStatusLocation(s: State)(status: StatusId, deep: Boolean = false): Option[Location] = {
    status(s).target match {
      case target: ContainerId => Some(target(s).location)
      case target: EntityId if deep => getEntityLocation(s)(target, deep)
      case _ => None
    }
  }

  def getTargetStatuses(s: State)(target: EntityId): Set[StatusId] = {
    s.index.targetStatuses.getOrElse(target, Set())
  }
}
