package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.State

case class TerrainKindId(value: String) extends KindId {
  def apply(s: State): TerrainKind = s.assets.kinds.terrains(this)
}
