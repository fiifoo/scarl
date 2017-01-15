package fi.fiifoo.scarl.core.entity

import fi.fiifoo.scarl.core.State

trait StatusId extends EntityId {

  override def apply(s: State): Status = s.entities(this).asInstanceOf[Status]
}
