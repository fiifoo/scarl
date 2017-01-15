package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.State

trait EntityId {
  val value: Int

  def apply(s: State): Entity = s.entities(this)
}
