package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.kind.TerrainKindId

case class Terrain(id: TerrainId,
                   kind: TerrainKindId,
                   location: Location
                  ) extends Entity with Locatable {

  def setLocation(location: Location): Locatable = copy(location = location)
}
