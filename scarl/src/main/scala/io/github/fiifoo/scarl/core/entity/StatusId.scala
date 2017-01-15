package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.State

trait StatusId extends EntityId {

  override def apply(s: State): Status = s.entities(this).asInstanceOf[Status]
}
