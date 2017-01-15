package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.State

trait ActorId extends EntityId {

  override def apply(s: State): Actor = s.entities(this).asInstanceOf[Actor]
}
