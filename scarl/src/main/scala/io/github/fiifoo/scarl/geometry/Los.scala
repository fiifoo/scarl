package io.github.fiifoo.scarl.geometry

import io.github.fiifoo.scarl.core.{Location, State}

object Los {
  def apply(s: State)(line: Vector[Location]): Boolean = {
    val obstacle = Obstacle.sight(s) _

    !line.tail.dropRight(1).exists(obstacle(_).isDefined)
  }
}
