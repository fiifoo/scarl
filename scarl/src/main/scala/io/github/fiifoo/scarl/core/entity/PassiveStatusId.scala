package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.State

case class PassiveStatusId(value: Int) extends EntityId with StatusId {

  override def apply(s: State): PassiveStatus = s.entities(this).asInstanceOf[PassiveStatus]
}
