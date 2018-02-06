package io.github.fiifoo.scarl.core.geometry

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.geometry.WaypointNetwork.Waypoint

import scala.annotation.tailrec
import scala.collection.immutable.Queue

/**
  * Start waypoint is excluded and end included
  */
object WaypointPath {

  def apply(s: State)(from: Waypoint, to: Waypoint): Option[Vector[Waypoint]] = {
    find(s)(from, _ == to)
  }

  def find(s: State)(from: Waypoint, to: Waypoint => Boolean, exclude: Set[Waypoint] = Set()): Option[Vector[Waypoint]] = {
    val network = s.cache.waypointNetwork

    @tailrec
    def travel(paths: Queue[Vector[Waypoint]], exclude: Set[Waypoint]): Option[Vector[Waypoint]] = {
      val (path, dequeued) = paths.dequeue
      val waypoint = path.last

      if (to(waypoint)) {
        Some(path)
      } else {
        val adjacent = network.adjacentWaypoints.getOrElse(waypoint, Set()) -- exclude
        val enqueued = dequeued enqueue (adjacent map (path :+ _))

        if (enqueued.isEmpty) {
          None
        } else {
          travel(enqueued, exclude ++ adjacent)
        }
      }
    }

    val result = travel(Queue(Vector(from)), exclude + from) map (_.tail)

    if (result exists (_.isEmpty)) {
      None
    } else {
      result
    }
  }

}
