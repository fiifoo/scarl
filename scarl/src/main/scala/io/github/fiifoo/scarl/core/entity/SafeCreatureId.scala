package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.State

object SafeCreatureId {
  def apply(id: CreatureId): SafeCreatureId = SafeCreatureId(id.value)
}

case class SafeCreatureId(value: Int) {

  def apply(s: State): Option[Creature] = {
    val id = CreatureId(value)
    s.entities.get(id) map (_.asInstanceOf[Creature])
  }
}
