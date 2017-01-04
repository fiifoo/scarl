package fi.fiifoo.scarl.core

import fi.fiifoo.scarl.core.entity.{EntityId, ItemId, LocatableId, StatusId}

case class StateIndex(entities: EntityIndex = EntityIndex(),
                      items: ItemIndex = ItemIndex(),
                      statuses: StatusIndex = StatusIndex()
                     )

case class EntityIndex(location: Map[Location, List[LocatableId]] = Map())

case class ItemIndex(container: Map[EntityId, List[ItemId]] = Map())

case class StatusIndex(target: Map[EntityId, List[StatusId]] = Map())
