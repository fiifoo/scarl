package fi.fiifoo.scarl.core.entity

import fi.fiifoo.scarl.core.State

trait EntityId {
  val value: Int

  def apply(s: State): Entity = s.entities(this)
}
