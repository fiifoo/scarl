package fi.fiifoo.scarl.status

import fi.fiifoo.scarl.core.entity.{CreatureId, PassiveStatus, PassiveStatusId}

case class DeathStatus(id: PassiveStatusId, target: CreatureId) extends PassiveStatus
