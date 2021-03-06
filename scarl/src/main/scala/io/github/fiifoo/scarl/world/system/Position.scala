package io.github.fiifoo.scarl.world.system

case class Position(x: Double, y: Double) {

  def add(other: Position): Position = {
    Position(this.x + other.x, this.y + other.y)
  }

  def add(vector: Vector): Position = {
    Position(this.x + vector.x, this.y + vector.y)
  }
}
