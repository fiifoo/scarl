package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.Location

case class Terrain(id: TerrainId, location: Location) extends Entity with Locatable {

  def setLocation(location: Location): Locatable = copy(location = location)
}
