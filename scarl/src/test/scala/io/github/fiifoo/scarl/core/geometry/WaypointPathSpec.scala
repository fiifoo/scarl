package io.github.fiifoo.scarl.core.geometry

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.geometry.WaypointNetwork.Waypoint
import org.scalatest._

class WaypointPathSpec extends FlatSpec with Matchers {

  "WaypointPath" should "find shortest path from one waypoint to another" in {
    val s = State(cache = State.Cache(
      waypointNetwork = network
    ))

    tests foreach (test => {
      val (from, to, result) = test

      WaypointPath(s)(from, to) should ===(result)
    })
  }

  lazy private val network: WaypointNetwork = WaypointNetwork(adjacentWaypoints = Map(
    Location(0, 0) -> Set(Location(1, 1)),
    Location(1, 1) -> Set(Location(0, 0), Location(2, 2), Location(3, 3)),
    Location(2, 2) -> Set(Location(1, 1), Location(4, 4)),
    Location(3, 3) -> Set(Location(1, 1), Location(5, 5)),
    Location(4, 4) -> Set(Location(2, 2), Location(5, 5)),
    Location(5, 5) -> Set(Location(3, 3), Location(4, 4)),

    Location(10, 10) -> Set[Location](),
  ))

  lazy private val tests: List[(Waypoint, Waypoint, Option[Vector[Waypoint]])] = List((
    Location(0, 0),
    Location(0, 0),
    None
  ), (
    Location(1000, 1000),
    Location(1000, 1001),
    None
  ), (
    Location(0, 0),
    Location(1, 1),
    Some(Vector(Location(1, 1)))
  ), (
    Location(0, 0),
    Location(2, 2),
    Some(Vector(Location(1, 1), Location(2, 2)))
  ), (
    Location(0, 0),
    Location(3, 3),
    Some(Vector(Location(1, 1), Location(3, 3)))
  ), (
    Location(0, 0),
    Location(4, 4),
    Some(Vector(Location(1, 1), Location(2, 2), Location(4, 4)))
  ), (
    Location(0, 0),
    Location(5, 5),
    Some(Vector(Location(1, 1), Location(3, 3), Location(5, 5)))
  ), (
    Location(0, 0),
    Location(10, 10),
    None
  ))
}
