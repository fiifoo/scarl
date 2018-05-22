package io.github.fiifoo.scarl.area.shape

import io.github.fiifoo.scarl.area.shape.Shape.Result
import io.github.fiifoo.scarl.core.geometry.{Location, Rotation}

import scala.util.Random

object Shape {

  case class Result(outerWidth: Int,
                    outerHeight: Int,
                    innerWidth: Int,
                    innerHeight: Int,
                    border: Set[Location],
                    contained: Set[Location],
                    entranceCandidates: Set[Location]
                   ) {

    def rotate(rotation: Rotation): Result = {
      rotation.value match {
        case 0 => this
        case 1 | 3 => this.copy(
          outerWidth = this.outerHeight,
          outerHeight = this.outerWidth,
          innerWidth = this.innerHeight,
          innerHeight = this.innerWidth,
          border = this.border map rotation.apply,
          contained = this.contained map rotation.apply,
          entranceCandidates = this.entranceCandidates map rotation.apply
        )
        case 2 => this.copy(
          border = this.border map rotation.apply,
          contained = this.contained map rotation.apply,
          entranceCandidates = this.entranceCandidates map rotation.apply
        )
      }
    }
  }

}

trait Shape {
  def apply(random: Random): Result
}
