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
    if (from == to) {
      return None
    }

    val network = s.cache.waypointNetwork

    @tailrec
    def travel(paths: Queue[Vector[Waypoint]], visited: Set[Waypoint]): Option[Vector[Waypoint]] = {
      val (path, dequeued) = paths.dequeue
      val waypoint = path.last

      if (waypoint == to) {
        Some(path)
      } else {
        val adjacent = network.adjacentWaypoints.getOrElse(waypoint, Set()) -- visited
        val enqueued = dequeued enqueue (adjacent map (path :+ _))

        if (enqueued.isEmpty) {
          None
        } else {
          travel(enqueued, visited ++ adjacent)
        }
      }
    }

    travel(Queue(Vector(from)), Set(from)) map (_.tail)
  }
}
