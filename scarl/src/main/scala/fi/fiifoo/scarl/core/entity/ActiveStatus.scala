package fi.fiifoo.scarl.core.entity

import fi.fiifoo.scarl.core.State
import fi.fiifoo.scarl.core.effect.Effect

trait ActiveStatus extends Entity with Status with Actor {
  val id: ActiveStatusId

  def activate(s: State): List[Effect]
}
