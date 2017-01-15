package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.State

case class WallId(value: Int) extends EntityId with LocatableId {

  override def apply(s: State): Wall = s.entities(this).asInstanceOf[Wall]
}
