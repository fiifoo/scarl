package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.State

trait KindId {
  val value: String

  def apply(s: State): Kind
}
