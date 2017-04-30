package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.entity.{ActiveStatus, Entity, Status}
import io.github.fiifoo.scarl.core.world.Traveler
import io.github.fiifoo.scarl.core.{Location, State}

case class ConduitExitMutation(traveler: Traveler, location: Location) extends Mutation {

  def apply(s: State): State = {
    var r = add(s, traveler.creature.copy(
      location = location,
      tick = s.tick
    ))

    r = traveler.items.foldLeft(r)(add)
    r = getStatuses(s).foldLeft(r)(add)

    r = traveler.equipments.foldLeft(r)((r, x) => {
      val (slot, item) = x

      EquipItemMutation(traveler.creature.id, item, Set(slot))(r)
    })

    r
  }

  private def add(s: State, entity: Entity): State = {
    NewEntityMutation(entity, existing = true)(s)
  }

  private def getStatuses(s: State): Set[Status] = {
    traveler.statuses collect {
      case status: ActiveStatus => setStatusTick(s, status)
      case status: Status => status
    }
  }

  private def setStatusTick(s: State, status: ActiveStatus): ActiveStatus = {
    val diff = status.tick - traveler.creature.tick
    val next = s.tick + Math.max(0, diff)

    status.setTick(next)
  }
}
