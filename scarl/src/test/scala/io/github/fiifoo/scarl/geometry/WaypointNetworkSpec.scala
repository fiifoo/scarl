package io.github.fiifoo.scarl.geometry

import io.github.fiifoo.scarl.core.State.Area
import io.github.fiifoo.scarl.core.entity.{Wall, WallId}
import io.github.fiifoo.scarl.core.kind.WallKindId
import io.github.fiifoo.scarl.core.mutation.{IdSeqMutation, NewEntityMutation}
import io.github.fiifoo.scarl.core.{Location, State}
import org.scalatest._

class WaypointNetworkSpec extends FlatSpec with Matchers {

  "WaypointNetwork" should "calculate waypoint for simple sector" in {
    val s = getState(Area(2, 2, 2), Set(
      Location(1, 1)
    ))

    WaypointNetwork(s) should ===(WaypointNetwork(
      waypoints = Set(Location(0, 0)),
      adjacentWaypoints = Map(),
      locationWaypoint = Map(
        Location(0, 0) -> Location(0, 0),
        Location(0, 1) -> Location(0, 0),
        Location(1, 0) -> Location(0, 0)
      ),
      waypointLocations = Map(Location(0, 0) -> Set(
        Location(0, 0),
        Location(0, 1),
        Location(1, 0)
      ))
    ))
  }

  it should "calculate two waypoints for divided sector" in {
    val s = getState(Area(3, 3, 3), Set(
      Location(1, 0),
      Location(1, 1),
      Location(1, 2)
    ))

    WaypointNetwork(s).waypoints should ===(Set(
      Location(0, 0),
      Location(2, 0)
    ))
  }

  it should "calculate adjacent waypoints" in {
    val s = getState(Area(6, 3, 3), Set(
      Location(1, 0),
      Location(1, 1),
      Location(1, 2)
    ))

    val result = WaypointNetwork(s)

    result.waypoints should ===(Set(
      Location(0, 0),
      Location(2, 0),
      Location(4, 1)
    ))

    result.adjacentWaypoints should ===(Map(
      Location(2, 0) -> Set(Location(4, 1)),
      Location(4, 1) -> Set(Location(2, 0))
    ))
  }

  private def getState(area: Area, walls: Set[Location]): State = {
    val s = State(area = area)

    walls.foldLeft(s)((s, location) => {
      val (nextId, nextIdSeq) = s.idSeq()
      val wall = Wall(WallId(nextId), WallKindId("wall"), location)

      NewEntityMutation(wall)(IdSeqMutation(nextIdSeq)(s))
    })
  }
}
