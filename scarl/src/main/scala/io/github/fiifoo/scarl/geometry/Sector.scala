package io.github.fiifoo.scarl.geometry

import io.github.fiifoo.scarl.core.{Location, State}

object Sector {

  def apply(location: Location, size: Int): Sector = {
    val x = ((location.x: Double) / size).floor.toInt
    val y = ((location.y: Double) / size).floor.toInt

    Sector(x, y, size)
  }

  def sectors(s: State): Set[Sector] = {
    def dimension(areaSize: Int): Set[Int] = {
      val count = ((areaSize: Double) / s.area.sectorSize).ceil.toInt

      (0 until count).toSet
    }

    dimension(s.area.width) flatMap (x => {
      dimension(s.area.height) map (y => {
        Sector(x, y, s.area.sectorSize)
      })
    })
  }

}

case class Sector(x: Int, y: Int, size: Int) {

  def center: Location = {
    Location(
      x * size + ((size - 1) / 2),
      y * size + ((size - 1) / 2)
    )
  }

  def locations: Set[Location] = {
    val location = Location(x * size, y * size)

    Shape.square(location, size)
  }
}
