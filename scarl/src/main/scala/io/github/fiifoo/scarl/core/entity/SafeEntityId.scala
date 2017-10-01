package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.State

sealed trait SafeEntityId {
  def apply(s: State): Option[Entity]
}

object SafeCreatureId {
  def apply(id: CreatureId): SafeCreatureId = SafeCreatureId(id.value)
}

case class SafeCreatureId(value: Int) extends SafeEntityId {
  def apply(s: State): Option[Creature] = s.entities.get(CreatureId(value)) map (_.asInstanceOf[Creature])
}
