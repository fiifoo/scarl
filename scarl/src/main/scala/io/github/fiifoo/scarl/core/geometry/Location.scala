package io.github.fiifoo.scarl.core.geometry

case class Location(x: Int, y: Int) {
  def add(location: Location): Location = Location(x + location.x, y + location.y)

  def sub(location: Location): Location = Location(x - location.x, y - location.y)

  def adjacent: Set[Location] = {
    Set(
      Location(x, y + 1),
      Location(x + 1, y + 1),
      Location(x + 1, y),
      Location(x + 1, y - 1),
      Location(x, y - 1),
      Location(x - 1, y - 1),
      Location(x - 1, y),
      Location(x - 1, y + 1)
    )
  }

  def closest(locations: Set[Location]): Option[Location] = {
    locations.reduceLeftOption((a, b) => {
      val ad = Distance(this, a)
      val bd = Distance(this, b)

      if (ad == bd) {
        val av = a.x + a.y
        val bv = b.x + b.y

        if (av == bv) {
          if (a.x < b.x) a else b
        } else {
          if (av < bv) a else b
        }
      } else {
        if (ad < bd) a else b
      }
    })
  }
}
