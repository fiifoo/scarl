package io.github.fiifoo.scarl.area.shape

import io.github.fiifoo.scarl.area.shape.Shape.Result
import io.github.fiifoo.scarl.core.geometry.Location

import scala.util.Random

object Shape {

  case class Result(outerWidth: Int,
                    outerHeight: Int,
                    innerWidth: Int,
                    innerHeight: Int,
                    border: Set[Location],
                    contained: Set[Location],
                    entranceCandidates: Set[Location]
                   )

}

trait Shape {
  def apply(random: Random): Result
}
