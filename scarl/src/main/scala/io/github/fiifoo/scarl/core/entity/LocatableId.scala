package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.State

trait LocatableId extends EntityId {

  override def apply(s: State): Locatable = s.entities(this).asInstanceOf[Locatable]
}
