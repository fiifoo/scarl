package io.github.fiifoo.scarl.geometry

import io.github.fiifoo.scarl.core.Location

object Shape {

  def circle(location: Location, radius: Int): Set[Location] = {
    val range = 0 to radius * 2

    range.foldLeft(Set[Location]())((result, y) => {
      val row = range map (x => {
        location.add(Location(x - radius, y - radius))
      })

      result ++ row.toSet
    })
  }

}
