package fi.fiifoo.scarl.core.mutation

import fi.fiifoo.scarl.core._
import fi.fiifoo.scarl.core.entity.{Entity, Item, Locatable, Status}
import fi.fiifoo.scarl.core.mutation.index.{ItemContainerIndexRemoveMutation, LocatableLocationIndexRemoveMutation, StatusTargetIndexRemoveMutation}

case class RemoveEntitiesMutation() extends Mutation {

  def apply(s: State): State = {

    val entities = s.tmp.removableEntities map (id => s.entities(id))
    val index = entities.foldLeft(s.index)((index, entity) => {
      mutateIndex(index, entity)
    })

    s.copy(
      entities = s.entities -- s.tmp.removableEntities,
      index = index,
      tmp = s.tmp.copy(removableEntities = List())
    )
  }

  private def mutateIndex(index: StateIndex, entity: Entity): StateIndex = {
    index.copy(
      entities = mutateEntityIndex(index.entities, entity),
      statuses = entity match {
        case status: Status => mutateStatusIndex(index.statuses, status)
        case _ => index.statuses
      },
      items = entity match {
        case item: Item => mutateItemIndex(index.items, item)
        case _ => index.items
      }
    )
  }

  private def mutateEntityIndex(index: EntityIndex, entity: Entity): EntityIndex = {
    index.copy(
      location = entity match {
        case locatable: Locatable => LocatableLocationIndexRemoveMutation(locatable.id, locatable.location)(index.location)
        case _ => index.location
      }
    )
  }

  private def mutateStatusIndex(index: StatusIndex, status: Status): StatusIndex = {
    index.copy(
      target = StatusTargetIndexRemoveMutation(status.id, status.target)(index.target)
    )
  }

  private def mutateItemIndex(index: ItemIndex, item: Item): ItemIndex = {
    index.copy(
      container = ItemContainerIndexRemoveMutation(item.id, item.container)(index.container)
    )
  }
}
