package io.github.fiifoo.scarl.geometry

import io.github.fiifoo.scarl.core.Location

object Distance {

  def apply(from: Location, to: Location): Int = {
    chebyshev(from, to)
  }

  def chebyshev(from: Location, to: Location): Int = {
    Math.max(Math.abs(from.x - to.x), Math.abs(from.y - to.y))
  }

  def manhattan(from: Location, to: Location): Int = {
    Math.abs(from.x - to.x) + Math.abs(from.y - to.y)
  }
}
