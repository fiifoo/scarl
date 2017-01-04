package fi.fiifoo.scarl.core.entity

import fi.fiifoo.scarl.core.State

trait ActorId extends EntityId {

  override def apply(s: State): Actor = s.entities(this).asInstanceOf[Actor]
}
