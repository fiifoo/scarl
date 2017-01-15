package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.State

case class ItemId(value: Int) extends EntityId {

  override def apply(s: State): Item = s.entities(this).asInstanceOf[Item]
}
