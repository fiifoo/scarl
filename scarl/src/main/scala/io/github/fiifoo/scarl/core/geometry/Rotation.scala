package io.github.fiifoo.scarl.core.geometry

import scala.util.Random

object Rotation {
  def apply(random: Random, width: Int, height: Int): Rotation = {
    Rotation(random.nextInt(4), width, height)
  }
}

case class Rotation(value: Int, width: Int, height: Int) {

  def next: Rotation = {
    if (this.value == 3) {
      this.copy(value = 0)
    } else {
      this.copy(value = this.value + 1)
    }
  }

  def reverse: Rotation = {
    if (this.value == 0 || this.value == 2) {
      this
    } else {
      Rotation(4 - this.value, this.height, this.width)
    }
  }

  def shape: (Int, Int) = {
    this.value match {
      case 0 | 2 => (this.width, this.height)
      case 1 | 3 => (this.height, this.width)
    }
  }

  def apply(l: Location): Location = {
    val width = if (this.width == 0) 0 else this.width - 1
    val height = if (this.height == 0) 0 else this.height - 1

    this.value match {
      case 0 => l
      case 1 => Location(l.y, -l.x) add Location(0, height)
      case 2 => Location(-l.x, -l.y) add Location(width, height)
      case 3 => Location(-l.y, l.x) add Location(width, 0)
    }
  }

  def mapKey[T](xs: Map[Location, T]): Map[Location, T] = {
    xs map (x => this.apply(x._1) -> x._2)
  }
}
