package io.github.fiifoo.scarl.status

import io.github.fiifoo.scarl.core.creature.Stance
import io.github.fiifoo.scarl.core.entity._

case class CreatureStanceStatus(id: PassiveStatusId,
                                target: CreatureId,
                                stance: Stance,
                                continuous: Boolean,
                                duration: Option[Int],
                                active: Boolean = true,
                               ) extends PassiveStatus with StanceStatus {

  def setDuration(duration: Option[Int]): CreatureStanceStatus = {
    this.copy(duration = duration)
  }

  def setActive(active: Boolean): CreatureStanceStatus = {
    this.copy(active = active)
  }
}
