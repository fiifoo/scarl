package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.entity.{Terrain, TerrainId}
import io.github.fiifoo.scarl.core.{Location, State}

case class TerrainKind(id: TerrainKindId,
                       name: String,
                       display: Char,
                       color: String
                      ) extends Kind {

  def apply(s: State, location: Location): Terrain = {
    Terrain(
      id = TerrainId(s.nextEntityId),
      kind = id,
      location = location
    )
  }
}
