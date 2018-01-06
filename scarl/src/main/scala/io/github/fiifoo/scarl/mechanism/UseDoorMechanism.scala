package io.github.fiifoo.scarl.mechanism

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Machinery
import io.github.fiifoo.scarl.core.entity.Selectors.getLocationItems
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item.Mechanism
import io.github.fiifoo.scarl.effect.interact.UseDoorEffect

case class UseDoorMechanism(disposable: Boolean) extends Mechanism {

  def interact(s: State, machinery: Machinery, control: Location): List[Effect] = {
    val doors = machinery.targets flatMap getLocationItems(s) filter (_ (s).door.isDefined)
    val effects = (doors map (UseDoorEffect(user = None, _))).toList

    activate(machinery, effects)
  }
}
