package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.Location

case class Container(id: ContainerId, location: Location) extends Entity with Locatable {

  def setLocation(location: Location): Container = copy(location = location)
}
