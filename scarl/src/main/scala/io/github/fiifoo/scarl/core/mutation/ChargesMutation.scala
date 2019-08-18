package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.{Chargeable, EntityId}

case class ChargesMutation(entity: EntityId, charges: Int) extends Mutation {

  def apply(s: State): State = {
    s.entities.get(entity) collect {
      case chargeable: Chargeable => chargeable
    } map (chargeable => {
      val charge = chargeable.charge map (_.copy(charges = charges))

      s.copy(entities = s.entities + (chargeable.id -> chargeable.setCharge(charge)))
    }) getOrElse {
      s
    }
  }
}
