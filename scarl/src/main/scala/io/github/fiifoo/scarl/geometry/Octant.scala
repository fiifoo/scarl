package io.github.fiifoo.scarl.geometry

import io.github.fiifoo.scarl.core.Location

/*

  \2|1/
  3\|/0
 ---+---
  4/|\7
  /5|6\

*/

object Octant {
  def apply(a: Location, b: Location): Octant = {
    new Octant(calc(a, b))
  }

  private def calc(a: Location, b: Location): Int = {
    val dx: Double = b.x - a.x
    val dy: Double = b.y - a.y

    if (dy == 0) {
      return if (dx >= 0) 0 else 4
    }
    if (dx == 0) {
      return if (dy >= 0) 2 else 6
    }

    val s = Math.abs(dy / dx)

    if (dy > 0) {
      if (dx > 0) {
        if (s < 1) 0 else 1
      } else {
        if (s > 1) 2 else 3
      }
    } else {
      if (dx < 0) {
        if (s < 1) 4 else 5
      } else {
        if (s > 1) 6 else 7
      }
    }
  }
}

case class Octant(value: Int) {

  def normalize(l: Location): Location = {
    value match {
      case 0 => l
      case 1 => Location(l.y, l.x)
      case 2 => Location(l.y, -l.x)
      case 3 => Location(-l.x, l.y)
      case 4 => Location(-l.x, -l.y)
      case 5 => Location(-l.y, -l.x)
      case 6 => Location(-l.y, l.x)
      case 7 => Location(l.x, -l.y)
    }
  }

  def denormalize(l: Location): Location = {
    value match {
      case 0 => l
      case 1 => Location(l.y, l.x)
      case 2 => Location(-l.y, l.x)
      case 3 => Location(-l.x, l.y)
      case 4 => Location(-l.x, -l.y)
      case 5 => Location(-l.y, -l.x)
      case 6 => Location(l.y, -l.x)
      case 7 => Location(l.x, -l.y)
    }
  }
}
