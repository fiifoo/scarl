package fi.fiifoo.scarl.core.entity

import fi.fiifoo.scarl.core.State

case class ItemId(value: Int) extends EntityId {

  override def apply(s: State): Item = s.entities(this).asInstanceOf[Item]
}
