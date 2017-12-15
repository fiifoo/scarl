package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.Selectors.{getContainerItems, getTargetStatuses}
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.{CreatureId, EntityId, ItemId, StatusId}
import io.github.fiifoo.scarl.core.world.{ConduitId, Traveler}

case class ConduitEntryMutation(creature: CreatureId, conduit: ConduitId) extends Mutation {
  def apply(s: State): State = {
    if (s.tmp.conduitEntry.isDefined) {
      throw new Exception("Conduit entry exists. Only one allowed.")
    }

    // Items inside statuses not supported. Don't allow at model?
    val items = getItems(s, creature)
    // Supports statuses targeting statuses. Really needed?
    val statuses = getStatuses(s, creature) ++ (items flatMap (getStatuses(s, _)))

    val traveler = Traveler(
      creature = creature(s),
      items = items map (_ (s)),
      statuses = statuses map (_ (s)),
      equipments = s.equipments getOrElse(creature, Map()),
      keys = s.keys.getOrElse(creature, Set())
    )

    s.copy(tmp = s.tmp.copy(
      conduitEntry = Some((conduit, traveler))
    ))
  }

  private def getItems(s: State, container: EntityId): Set[ItemId] = {
    val items = getContainerItems(s)(container)

    if (items.nonEmpty) {
      items ++ (items flatMap (getItems(s, _)))
    } else {
      Set()
    }
  }

  private def getStatuses(s: State, target: EntityId): Set[StatusId] = {
    val statuses = getTargetStatuses(s)(target)

    if (statuses.nonEmpty) {
      statuses ++ (statuses flatMap (getStatuses(s, _)))
    } else {
      Set()
    }
  }
}
