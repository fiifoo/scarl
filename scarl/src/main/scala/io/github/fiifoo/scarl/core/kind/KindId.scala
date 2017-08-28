package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.Locatable

trait KindId[T <: Locatable] {
  val value: String

  def apply(s: State): Kind[T]
}
