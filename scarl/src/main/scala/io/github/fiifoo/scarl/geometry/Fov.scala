package io.github.fiifoo.scarl.geometry

import io.github.fiifoo.scarl.core.{Location, State}

object Fov {

  def apply(s: State)(start: Location, range: Int): List[Location] = {
    val los = Los(s) _
    var fov: List[Location] = List()

    for (x <- 0 to range * 2) {
      for (y <- 0 to range * 2) {
        val location = Location(start.x - range + x, start.y - range + y)
        if (los(Line(start, location))) {
          fov = location :: fov
        }
      }
    }

    fov
  }
}
