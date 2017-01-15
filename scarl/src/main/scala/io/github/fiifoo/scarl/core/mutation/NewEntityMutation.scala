package fi.fiifoo.scarl.core.mutation

import fi.fiifoo.scarl.core._
import fi.fiifoo.scarl.core.entity._
import fi.fiifoo.scarl.core.mutation.index.{ItemContainerIndexAddMutation, LocatableLocationIndexAddMutation, StatusTargetIndexAddMutation}

case class NewEntityMutation(entity: Entity) extends Mutation {

  def apply(s: State): State = {
    if (s.entities.isDefinedAt(entity.id)) {
      throw new Exception(s"${entity.id} already exists")
    }
    if (entity.id.value != s.nextEntityId) {
      throw new Exception(s"${entity.id} does not match state next entity id")
    }

    val addedActors = entity match {
      case actor: Actor => actor.id :: s.tmp.addedActors
      case _ => s.tmp.addedActors
    }

    s.copy(
      entities = s.entities + (entity.id -> entity),
      index = mutateIndex(s.index, entity),
      nextEntityId = entity.id.value + 1,
      tmp = s.tmp.copy(addedActors = addedActors)
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
        case locatable: Locatable => LocatableLocationIndexAddMutation(locatable.id, locatable.location)(index.location)
        case _ => index.location
      }
    )
  }

  private def mutateStatusIndex(index: StatusIndex, status: Status): StatusIndex = {
    index.copy(
      target = StatusTargetIndexAddMutation(status.id, status.target)(index.target)
    )
  }

  private def mutateItemIndex(index: ItemIndex, item: Item): ItemIndex = {
    index.copy(
      container = ItemContainerIndexAddMutation(item.id, item.container)(index.container)
    )
  }

}
