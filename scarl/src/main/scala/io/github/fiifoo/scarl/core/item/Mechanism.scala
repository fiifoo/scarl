package io.github.fiifoo.scarl.core.item

import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Machinery
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.effect.area.RemoveEntityEffect

trait Mechanism {
  val disposable: Boolean

  def interact(s: State, machinery: Machinery, control: Location): List[Effect]

  def activate(machinery: Machinery, effects: List[Effect]): List[Effect] = {
    if (disposable) {
      RemoveEntityEffect(machinery.id) :: effects
    } else {
      effects
    }
  }
}
