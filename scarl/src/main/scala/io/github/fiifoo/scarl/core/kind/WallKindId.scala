package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.Wall

case class WallKindId(value: String) extends KindId[Wall] {
  def apply(s: State): WallKind = s.assets.kinds.walls(this)
}
