package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect

trait ActiveStatus extends Entity with Status with Actor {
  val id: ActiveStatusId

  def apply(s: State): List[Effect]

  def setTick(tick: Int): ActiveStatus
}
