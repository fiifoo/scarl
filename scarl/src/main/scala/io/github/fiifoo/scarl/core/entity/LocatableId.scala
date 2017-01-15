package fi.fiifoo.scarl.core.entity

import fi.fiifoo.scarl.core.State

trait LocatableId extends EntityId {

  override def apply(s: State): Locatable = s.entities(this).asInstanceOf[Locatable]
}
