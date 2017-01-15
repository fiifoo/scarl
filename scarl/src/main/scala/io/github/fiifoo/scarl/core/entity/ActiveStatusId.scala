package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.State

case class ActiveStatusId(value: Int) extends EntityId with StatusId with ActorId {

  override def apply(s: State): ActiveStatus = s.entities(this).asInstanceOf[ActiveStatus]
}
