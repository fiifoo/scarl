package fi.fiifoo.scarl.core.entity

import fi.fiifoo.scarl.core.State

case class ActiveStatusId(value: Int) extends EntityId with StatusId with ActorId {

  override def apply(s: State): ActiveStatus = s.entities(this).asInstanceOf[ActiveStatus]
}
