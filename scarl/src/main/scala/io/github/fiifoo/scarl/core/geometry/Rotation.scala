package io.github.fiifoo.scarl.core.geometry

import scala.util.Random

object Rotation {
  def apply(random: Random): Rotation = {
    Rotation(random.nextInt(4))
  }
}

case class Rotation(value: Int = 0) {

  def next: Rotation = {
    if (value == 3) {
      Rotation()
    } else {
      Rotation(value + 1)
    }
  }

  def normalize(l: Location): Location = {
    value match {
      case 0 => l
      case 1 => Location(-l.y, l.x)
      case 2 => Location(-l.x, -l.y)
      case 3 => Location(l.y, -l.x)
    }
  }

  def denormalize(l: Location): Location = {
    value match {
      case 0 => l
      case 1 => Location(l.y, -l.x)
      case 2 => Location(-l.x, -l.y)
      case 3 => Location(-l.y, l.x)
    }
  }
}
