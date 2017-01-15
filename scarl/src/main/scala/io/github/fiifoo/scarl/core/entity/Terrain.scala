package fi.fiifoo.scarl.core.entity

import fi.fiifoo.scarl.core.Location

case class Terrain(id: TerrainId, location: Location) extends Entity with Locatable {

  def setLocation(location: Location): Locatable = copy(location = location)
}
