package io.github.fiifoo.scarl.status

import io.github.fiifoo.scarl.core.entity.{CreatureId, PassiveStatus, PassiveStatusId}

case class DeathStatus(id: PassiveStatusId, target: CreatureId) extends PassiveStatus
