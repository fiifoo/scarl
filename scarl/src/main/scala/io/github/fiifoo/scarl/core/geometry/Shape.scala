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
    (0 until width).toSet flatMap ((x: Int) => {
      (0 until height).toSet map ((y: Int) => {
        Location(x, y) add location
      })
    })
  }

  def square(location: Location, size: Int): Set[Location] = {
    rectangle(location, size, size)
  }
}
