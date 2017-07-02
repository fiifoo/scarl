package io.github.fiifoo.scarl.geometry

import io.github.fiifoo.scarl.core.{Location, State}

object Fov {

  def apply(s: State)(from: Location, range: Int): Set[Location] = {
    val obstacle = Obstacle.sight(s) _

    val results = octants flatMap (octant => {
      def denormalize(l: Location): Location = from add octant.denormalize(l)

      def blocked(l: Location): Boolean = obstacle(denormalize(l)).isDefined

      val results = ScanRecursive(range, blocked)(1, 0, 1)

      results map denormalize
    })

    results + from
  }

  private def octants: Set[Octant] = {
    (0 to 7).toSet map Octant.apply
  }

  private case class ScanRecursive(range: Int, blocked: Location => Boolean) {
    type Slope = Double
    type Scan = (Slope, Slope)

    def apply(step: Int, min: Slope, max: Slope): Set[Location] = {
      val locations = getLocations(step, min, max)

      if (step < this.range) {
        val scans = getScans(min, max, locations)

        locations.toSet ++ (scans flatMap (scan => {
          this (step + 1, scan._1, scan._2)
        }))
      } else {
        locations.toSet
      }
    }

    private def getLocations(step: Int, min: Slope, max: Slope): Seq[Location] = {
      val x = step
      val y1 = (step * min).floor.toInt
      val y2 = (step * max).ceil.toInt

      (y1 to y2) map (Location(x, _))
    }

    private def getScans(min: Slope,
                         max: Slope,
                         locations: Seq[Location]
                        ): List[Scan] = {

      val foldLocations = locations.foldLeft[(List[Scan], Option[Slope])]((List(), None)) _

      val (scans, nextMinOption) = foldLocations((carry, location) => {
        val (scans, nextMinOption) = carry

        if (this.blocked(location)) {
          nextMinOption map (nextMin => {
            val nextMax = getMaxSlope(location, max)

            ((nextMin, nextMax) :: scans, None)
          }) getOrElse {
            (scans, None)
          }
        } else {
          if (nextMinOption.isEmpty) {
            val nextMin = getMinSlope(location, min)

            (scans, Some(nextMin))
          } else {
            (scans, nextMinOption)
          }
        }
      })

      nextMinOption map (nextMin => {
        (nextMin, max) :: scans
      }) getOrElse {
        scans
      }
    }

    private def getMinSlope(l: Location, current: Slope): Slope = {
      val dx = l.x - 0.5
      val dy = l.y - 0.5

      Math.max(current, dy / dx)
    }

    private def getMaxSlope(l: Location, current: Slope): Slope = {
      val dx = l.x + 0.5
      val dy = l.y - 0.5

      Math.min(current, dy / dx)
    }
  }

}
