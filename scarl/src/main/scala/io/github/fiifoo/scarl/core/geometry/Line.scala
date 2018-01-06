package io.github.fiifoo.scarl.core.geometry

object Line {

  /**
    * Float comparison >= 0.5
    */
  val ErrorLimit = 0.49999

  def apply(a: Location, b: Location): Vector[Location] = {
    val octant = Octant(a, b)

    val line = calc(octant.normalize(a), octant.normalize(b))

    line.map(octant.denormalize)
  }

  private def calc(a: Location, b: Location): Vector[Location] = {
    val dx: Double = b.x - a.x
    val dy: Double = b.y - a.y
    val de: Double = dy / dx

    var error: Double = 0
    var y = a.y
    var line: Vector[Location] = Vector()

    for (x <- a.x to b.x) {
      line = line :+ Location(x, y)
      error = error + de
      if (error > ErrorLimit) {
        y = y + 1
        error = error - 1
      }
    }

    line
  }
}
