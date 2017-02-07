package io.github.fiifoo.scarl.geometry

import io.github.fiifoo.scarl.core.{Location, State}

object Fov {

  def apply(s: State)(start: Location, range: Int): Set[Location] = {
    val los = Los(s) _
    val r = 0 to range * 2
    val fov: Set[Location] = Set()

    r.foldLeft(fov)((fov, x) => {
      r.foldLeft(fov)((fov, y) => {
        val location = Location(start.x - range + x, start.y - range + y)
        if (los(Line(start, location))) {
          fov + location
        } else {
          fov
        }
      })
    })
  }
}
