package io.github.fiifoo.scarl.geometry

import io.github.fiifoo.scarl.core.{Location, State}

object Fov {

  def apply(s: State)(from: Location, range: Int): Set[Location] = {
    val obstacle = Obstacle.sight(s) _

    val results = octants flatMap (octant => {
      def denormalize(l: Location): Location = from add octant.denormalize(l)

      def blocked(l: Location): Boolean = obstacle(denormalize(l)).isDefined

      val results = ScanRecursive(range, blocked)()

      results map denormalize
    })

    results + from
  }

  private def octants: Set[Octant] = {
    (0 to 7).toSet map Octant.apply
  }

  case class ScanRecursive(range: Int, blocked: Location => Boolean) {

    case class Border(slope: Double, peeking: Boolean = false)

    type Scan = (Border, Border)

    def apply(step: Int = 1,
              min: Border = Border(0),
              max: Border = Border(1)
             ): Set[Location] = {

      val locations = getLocations(step, min, max)

      val subResults = if (step < this.range) {
        val scans = getScans(min, max, locations)

        scans flatMap (scan => {
          this (step + 1, scan._1, scan._2)
        })
      } else {
        Set()
      }

      locations.toSet ++ subResults
    }

    private def getLocations(step: Int, min: Border, max: Border): Seq[Location] = {
      val x = step
      val y1 = (step * min.slope) + (if (min.peeking) 0.5 else 0)
      val y2 = (step * max.slope) - (if (max.peeking) 0.5 else 0)

      (y1.round.toInt to y2.round.toInt) map (Location(x, _))
    }

    private def getScans(min: Border,
                         max: Border,
                         locations: Seq[Location]
                        ): List[Scan] = {

      val initialScans = List[Scan]()
      val initialMin = locations.headOption.collect {
        case l: Location if !this.blocked(l) => min
      }

      val (scans, minOption) = locations.foldLeft((initialScans, initialMin))((carry, location) => {
        val (scans, minOption) = carry

        if (this.blocked(location)) {

          val nextScans = minOption map (nextMin => {
            (nextMin, getMaxBorder(location)) :: scans // blocking location after free location -> new scan
          }) getOrElse {
            scans // another blocking location
          }

          (nextScans, None)

        } else {

          val nextMinOption = if (minOption.isEmpty) {
            Some(getMinBorder(location)) // free location after blocking location -> prepare new scan
          } else {
            minOption // another free location
          }

          (scans, nextMinOption)
        }
      })

      minOption map (nextMin => {
        (nextMin, max) :: scans
      }) getOrElse {
        scans
      }
    }

    private def getMinBorder(l: Location): Border = {
      val dx = l.x - 0.5
      val dy = l.y - 1

      Border(dy / dx, peeking = true)
    }

    private def getMaxBorder(l: Location): Border = {
      val dx = l.x + 0.5
      val dy = l.y

      Border(dy / dx, peeking = true)
    }
  }

}
