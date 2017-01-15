package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.Location

trait Locatable extends Entity {
  val id: LocatableId
  val location: Location

  def setLocation(location: Location): Locatable
}
