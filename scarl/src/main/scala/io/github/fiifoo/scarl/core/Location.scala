package io.github.fiifoo.scarl.core

case class Location(x: Int, y: Int) {
  def add(location: Location): Location = Location(x + location.x, y + location.y)
}
