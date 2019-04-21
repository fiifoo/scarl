package io.github.fiifoo.scarl.core.geometry

object Shape {

  def circle(location: Location, radius: Int): Set[Location] = {
    val dimension = (0 to radius * 2).toSet

    dimension flatMap (x => {
      dimension map (y => {
        Location(x - radius, y - radius) add location
      })
    })
  }

  def rectangle(location: Location, width: Int, height: Int): Set[Location] = {
    Rectangle(location, width, height).locations
  }

  def square(location: Location, size: Int): Set[Location] = {
    rectangle(location, size, size)
  }

  case class Rectangle(location: Location, width: Int, height: Int) {
    def locations: Set[Location] = {
      (0 until this.width).toSet flatMap ((x: Int) => {
        (0 until this.height).toSet map ((y: Int) => {
          Location(x, y) add this.location
        })
      })
    }

    def intersects(other: Rectangle): Boolean = {
      this.intersectionSize(other) > 0
    }

    def intersectionSize(other: Rectangle): Int = {
      val x1 = math.max(this.location.x, other.location.x)
      val x2 = math.min(this.location.x + this.width, other.location.x + other.width)
      val dx = math.max(x2 - x1, 0)

      val y1 = math.max(this.location.y, other.location.y)
      val y2 = math.min(this.location.y + this.height, other.location.y + other.height)
      val dy = math.max(y2 - y1, 0)

      dx * dy
    }
  }

}
