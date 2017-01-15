package fi.fiifoo.scarl.core.entity

import fi.fiifoo.scarl.core.State

case class PassiveStatusId(value: Int) extends EntityId with StatusId {

  override def apply(s: State): PassiveStatus = s.entities(this).asInstanceOf[PassiveStatus]
}
