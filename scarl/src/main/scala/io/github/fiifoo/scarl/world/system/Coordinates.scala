package io.github.fiifoo.scarl.world.system

object Coordinates {

  object Quarter {
    def apply(a: Position, b: Position): Quarter = {
      val dx = b.x - a.x
      val dy = b.y - a.y

      val value = if (dy == 0) {
        if (dx >= 0) 0 else 2
      } else if (dx == 0) {
        if (dy >= 0) 1 else 3
      } else {
        if (dy > 0) {
          if (dx > 0) 0 else 1
        } else {
          if (dx < 0) 2 else 3
        }
      }

      Quarter(value)
    }
  }

  case class Quarter(value: Int) {

    def normalize(p: Position): Position = {
      this.value match {
        case 0 => p
        case 1 => Position(-p.x, p.y)
        case 2 => Position(-p.x, -p.y)
        case 3 => Position(p.x, -p.y)
      }
    }

    def normalize(v: Vector): Vector = {
      this.value match {
        case 0 => v
        case 1 => Vector(-v.x, v.y)
        case 2 => Vector(-v.x, -v.y)
        case 3 => Vector(v.x, -v.y)
      }
    }

    def rotate(x: Vector): Vector = {
      this.value match {
        case 0 | 2 => this.normalize(x).flip
        case 1 | 3 => this.normalize(x)
      }
    }
  }

}
