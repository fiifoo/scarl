package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.Terrain

case class TerrainKindId(value: String) extends KindId[Terrain] {
  def apply(s: State): TerrainKind = s.assets.kinds.terrains(this)
}
