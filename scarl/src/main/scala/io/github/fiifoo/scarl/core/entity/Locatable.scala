package fi.fiifoo.scarl.core.entity

import fi.fiifoo.scarl.core.Location

trait Locatable extends Entity {
  val id: LocatableId
  val location: Location

  def setLocation(location: Location): Locatable
}
