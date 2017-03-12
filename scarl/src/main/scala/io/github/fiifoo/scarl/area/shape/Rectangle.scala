package io.github.fiifoo.scarl.area.shape

import io.github.fiifoo.scarl.area.shape.Shape.Result
import io.github.fiifoo.scarl.core.Location

import scala.util.Random

case class Rectangle(width: Int, height: Int, variance: Double) extends Shape {

  def apply(random: Random): Result = {
    val realWidth = calculateDimension(width, random)
    val realHeight = calculateDimension(height, random)

    val border = calculateBorder(realWidth, realHeight)
    val contained = calculateContained(realWidth, realHeight)
    val entranceCandidates = border -- calculateCorners(realWidth, realHeight)

    Result(
      realWidth,
      realHeight,
      realWidth,
      realHeight,
      border,
      contained,
      entranceCandidates
    )
  }

  private def calculateBorder(width: Int, height: Int): Set[Location] = {
    val top = (0 until width) map (Location(_, 0))
    val bottom = (0 until width) map (Location(_, height - 1))
    val left = (0 until height) map (Location(0, _))
    val right = (0 until height) map (Location(width - 1, _))

    top.toSet ++ bottom.toSet ++ left.toSet ++ right.toSet
  }

  private def calculateContained(width: Int, height: Int): Set[Location] = {
    (1 until height - 1).foldLeft(Set[Location]())((contained, y) => {
      val row = (1 until width - 1) map (Location(_, y))

      contained ++ row.toSet
    })
  }

  private def calculateCorners(width: Int, height: Int): Set[Location] = {
    Set(
      Location(0, 0),
      Location(width - 1, 0),
      Location(width - 1, height - 1),
      Location(0, height - 1)
    )
  }

  private def calculateDimension(value: Int, random: Random): Int = {
    if (variance == 0) {
      return value
    }

    val varianceAmount = Math.round(value * variance).toInt
    val calculated = value + random.nextInt(varianceAmount * 2 + 1) - varianceAmount

    if (calculated < 1) {
      1
    } else {
      calculated
    }
  }
}
