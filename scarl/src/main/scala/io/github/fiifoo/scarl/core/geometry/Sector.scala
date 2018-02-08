package io.github.fiifoo.scarl.core.geometry

import io.github.fiifoo.scarl.core.State

object Sector {

  def apply(s: State)(location: Location): Sector = {
    val size = s.area.sectorSize
    val x = ((location.x: Double) / size).floor.toInt
    val y = ((location.y: Double) / size).floor.toInt

    Sector(x, y)
  }

  def sectors(s: State): Set[Sector] = {
    val size = s.area.sectorSize

    def dimension(areaSize: Int): Set[Int] = {
      val count = ((areaSize: Double) / size).ceil.toInt

      (0 until count).toSet
    }

    dimension(s.area.width) flatMap (x => {
      dimension(s.area.height) map (y => {
        Sector(x, y)
      })
    })
  }

}

case class Sector(x: Int, y: Int) {

  def center(s: State): Location = {
    val size = s.area.sectorSize

    Location(
      x * size + ((size - 1) / 2),
      y * size + ((size - 1) / 2)
    )
  }

  def locations(s: State): Set[Location] = {
    val size = s.area.sectorSize
    val location = Location(x * size, y * size)

    Shape.square(location, size)
  }
}
