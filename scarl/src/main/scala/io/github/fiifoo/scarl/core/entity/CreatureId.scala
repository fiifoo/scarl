package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.State

case class CreatureId(value: Int) extends EntityId with ActorId with LocatableId {

  override def apply(s: State): Creature = s.entities(this).asInstanceOf[Creature]
}
