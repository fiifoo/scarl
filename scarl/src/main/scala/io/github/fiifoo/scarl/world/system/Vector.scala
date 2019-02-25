package io.github.fiifoo.scarl.world.system

case class Vector(x: Double, y: Double) {

  def add(other: Vector): Vector = {
    Vector(this.x + other.x, this.y + other.y)
  }

  def multiply(m: Double): Vector = {
    Vector(this.x * m, this.y * m)
  }

  def flip: Vector = {
    Vector(-this.x, -this.y)
  }
}
