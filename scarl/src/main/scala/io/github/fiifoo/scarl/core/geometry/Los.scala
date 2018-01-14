package io.github.fiifoo.scarl.core.geometry

import io.github.fiifoo.scarl.core.State

object Los {
  def apply(s: State)(line: Vector[Location]): Boolean = {
    val obstacle = Obstacle.sight(s) _

    !line.tail.dropRight(1).exists(obstacle(_).isDefined)
  }

  def locations(s: State)(from: Location, to: Location, range: Int): Boolean = {
    if (Distance(from, to) > range) {
      false
    } else {
      Los(s)(Line(from, to))
    }
  }
}
