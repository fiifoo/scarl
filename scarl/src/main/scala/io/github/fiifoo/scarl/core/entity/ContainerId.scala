package fi.fiifoo.scarl.core.entity

import fi.fiifoo.scarl.core.State

case class ContainerId(value: Int) extends EntityId with LocatableId {

  override def apply(s: State): Container = s.entities(this).asInstanceOf[Container]
}
