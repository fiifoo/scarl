package io.github.fiifoo.scarl.core.item

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, RemoveEntityEffect}
import io.github.fiifoo.scarl.core.entity.Machinery
import io.github.fiifoo.scarl.core.geometry.Location

trait Mechanism {
  def interact(s: State, machinery: Machinery, control: Location): List[Effect]

  def activate(machinery: Machinery, effects: List[Effect]): List[Effect] = {
    if (machinery.disposable) {
      RemoveEntityEffect(machinery.id) :: effects
    } else {
      effects
    }
  }
}
