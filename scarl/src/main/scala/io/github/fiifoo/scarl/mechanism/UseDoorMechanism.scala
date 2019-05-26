package io.github.fiifoo.scarl.mechanism

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Selectors.getContainerItems
import io.github.fiifoo.scarl.core.entity.{Container, Machinery}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item.Mechanism
import io.github.fiifoo.scarl.effect.interact.UseDoorEffect

case object UseDoorMechanism extends Mechanism {
  def interact(s: State, machinery: Machinery, control: Location): List[Effect] = {
    val doors = (machinery.getTargetEntities(s) collect {
      case container: Container => getContainerItems(s)(container.id) filter (_ (s).door.isDefined)
    }).flatten

    val effects = (doors map (UseDoorEffect(user = None, _))).toList

    activate(machinery, effects)
  }
}
