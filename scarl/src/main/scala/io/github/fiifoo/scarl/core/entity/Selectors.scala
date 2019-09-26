package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.{Faction, FactionId, Stats}
import io.github.fiifoo.scarl.core.geometry.WaypointNetwork.Waypoint
import io.github.fiifoo.scarl.core.geometry.{Location, Sector}
import io.github.fiifoo.scarl.core.item.{Key, Lock, PrivateKey}

object Selectors {

  def getContainerItems(s: State)(container: EntityId): Set[ItemId] = {
    s.index.containerItems.getOrElse(container, Set())
  }

  def getCreatureConditionStatuses(s: State)(creature: CreatureId): Set[ConditionStatus] = {
    getTargetStatuses(s)(creature) map (_.apply(s)) collect {
      case status: ConditionStatus => status
    }
  }

  def getCreatureComrades(s: State)(creature: CreatureId): Set[CreatureId] = {
    getCreaturePartyMembers(s)(creature) - creature
  }

  def getCreatureHostileFactions(s: State)(creature: CreatureId): Set[FactionId] = {
    val faction = creature(s).faction

    getFactionHostileFactions(s)(faction)
  }

  def getCreatureKeys(s: State)(creature: CreatureId): Set[Key] = {
    val keys = s.creature.keys.getOrElse(creature, Set())

    keys + PrivateKey(creature)
  }

  def getCreaturePartyMembers(s: State)(creature: CreatureId): Set[CreatureId] = {
    val party = creature(s).party

    s.index.partyMembers(party)
  }

  def getCreatureStanceStatuses(s: State)(creature: CreatureId): Set[StanceStatus] = {
    getTargetStatuses(s)(creature) map (_.apply(s)) collect {
      case status: StanceStatus => status
    }
  }

  def getCreatureStats(s: State)(creature: CreatureId): Stats = {
    val calculate = getEquipmentStats(s)(creature).add _ andThen
      (stats => {
        (getCreatureConditionStatuses(s)(creature) foldLeft stats) ((stats, status) => {
          status.condition.modifyStats(stats, status.strength)
        })
      }) andThen
      (stats => {
        (getCreatureStanceStatuses(s)(creature) foldLeft stats) ((stats, status) => {
          status.stance.modifyStats(stats)
        })
      })

    calculate(creature(s).stats)
  }

  def getCreatureWaypoint(s: State)(creature: CreatureId): Option[Waypoint] = {
    getLocationWaypoint(s)(creature(s).location)
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
    s.cache.equipmentStats.getOrElse(creature, Stats())
  }

  def getFactionHostileFactions(s: State)(faction: FactionId): Set[FactionId] = {
    val dispositions = s.assets.factions(faction).dispositions ++
      s.factions.dispositions.getOrElse(faction, Map())

    dispositions.filter(_._2 == Faction.Hostile).keySet
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

  def getLocationVisibleItems(s: State, creature: CreatureId)(location: Location): Set[ItemId] = {
    def removable(entity: EntityId) = s.tmp.removableEntities contains entity

    getLocationItems(s)(location) filterNot removable filter isVisibleItem(s, creature)
  }

  def getLocationWaypoint(s: State)(location: Location): Option[Waypoint] = {
    s.cache.waypointNetwork.locationWaypoint.get(location)
  }

  def getLocationWidgets(s: State)(location: Location): Set[ContainerId] = {
    s.index.locationEntities.get(location) map (entities => {
      entities collect {
        case container: ContainerId if container(s).widget => container
      }
    }) getOrElse Set()
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

  def getWidgetItem(s: State)(container: ContainerId): Option[ItemId] = {
    getContainerItems(s)(container).headOption
  }

  def getWaypointCreatures(s: State)(waypoint: Waypoint): Set[CreatureId] = {
    val sector = Sector(s)(waypoint)
    val creatures = s.index.sectorCreatures.getOrElse(sector, Set())

    creatures filter (getCreatureWaypoint(s)(_) contains waypoint)
  }

  def hasKey(s: State)(creature: CreatureId)(key: Key): Boolean = {
    getCreatureKeys(s)(creature) contains key
  }

  def hasLockKey(s: State)(creature: CreatureId)(lock: Lock): Boolean = {
    (lock.key exists hasKey(s)(creature)) && (lock.sub forall hasLockKey(s)(creature))
  }

  def isVisibleItem(s: State, creature: CreatureId)(item: ItemId) = {
    !item(s).hidden || (s.creature.foundItems.get(creature) exists (_ contains item))
  }
}
