package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.State

case class WallKindId(value: String) extends KindId {
  def apply(s: State): WallKind = s.kinds.walls(this)
}
