package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect

trait ActiveStatus extends Entity with Status with Actor {
  val id: ActiveStatusId

  def activate(s: State): List[Effect]
}
