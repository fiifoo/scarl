package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.State

case class TriggerStatusId(value: Int) extends EntityId with StatusId {

  override def apply(s: State): TriggerStatus = s.entities(this).asInstanceOf[TriggerStatus]
}
