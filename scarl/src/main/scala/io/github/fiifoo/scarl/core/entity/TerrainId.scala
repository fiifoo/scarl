package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.State

case class TerrainId(value: Int) extends EntityId with LocatableId {

  override def apply(s: State): Terrain = s.entities(this).asInstanceOf[Terrain]
}
