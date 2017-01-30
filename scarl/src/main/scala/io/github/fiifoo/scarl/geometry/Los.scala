package io.github.fiifoo.scarl.geometry

import io.github.fiifoo.scarl.core.{Location, State}

object Los {

  def apply(s: State)(line: Vector[Location]): Boolean = {
    val _blocked = blocked(s) _
    !line.tail.dropRight(1).exists(_blocked)
  }

  private def blocked(s: State)(location: Location): Boolean = {
    s.index.locationEntities.isDefinedAt(location)
  }
}
