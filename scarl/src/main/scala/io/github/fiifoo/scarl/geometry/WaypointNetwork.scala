package io.github.fiifoo.scarl.geometry

import io.github.fiifoo.scarl.core.Selectors.getLocationEntities
import io.github.fiifoo.scarl.core.entity.WallId
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.geometry.WaypointNetwork.Waypoint

import scala.annotation.tailrec
import scala.collection.immutable.Queue

object WaypointNetwork {

  type Waypoint = Location

  def apply(s: State): WaypointNetwork = {
    def blocked(location: Location): Boolean = {
      getLocationEntities(s)(location) exists (_.isInstanceOf[WallId])
    }

    val sectors = Sector.sectors(s)
    val calculate = calculateSector(blocked) _

    sectors.foldLeft(WaypointNetwork())(calculate)
  }

  private def calculateSector(blocked: Location => Boolean)
                             (network: WaypointNetwork,
                              sector: Sector
                             ): WaypointNetwork = {
    val locations = sector.locations filterNot blocked

    calculateWaypoint(network, sector, locations)
  }

  private def calculateWaypoint(network: WaypointNetwork,
                                sector: Sector,
                                locations: Set[Location]
                               ): WaypointNetwork = {
    sector.center.closest(locations) map (waypoint => {
      val nextNetwork = calculateWaypointLocations(
        network = addWaypoint(network, waypoint),
        waypoint = waypoint,
        queue = Queue(waypoint),
        locations = locations
      )
      val nextLocations = locations -- nextNetwork.locations.keys

      if (nextLocations.nonEmpty) {
        calculateWaypoint(nextNetwork, sector, nextLocations)
      } else {
        nextNetwork
      }
    }) getOrElse network
  }

  @tailrec
  private def calculateWaypointLocations(network: WaypointNetwork,
                                         waypoint: Waypoint,
                                         queue: Queue[Location],
                                         locations: Set[Location]
                                        ): WaypointNetwork = {
    if (queue.isEmpty) {
      return network
    }

    val (location, dequeuedQueue) = queue.dequeue
    val (nextNetwork, nextQueue) = location.adjacent.foldLeft((network, dequeuedQueue))((carry, adjacentLocation) => {
      val (currentNetwork, currentQueue) = carry
      processAdjacentLocation(currentNetwork, waypoint, currentQueue, locations, adjacentLocation)
    })

    calculateWaypointLocations(nextNetwork, waypoint, nextQueue, locations)
  }

  private def processAdjacentLocation(network: WaypointNetwork,
                                      waypoint: Waypoint,
                                      queue: Queue[Location],
                                      locations: Set[Location],
                                      location: Location
                                     ): (WaypointNetwork, Queue[Location]) = {

    (network.locations.get(location) map (existing => {
      if (existing != waypoint) {
        (addAdjacent(network, waypoint, existing), queue)
      } else {
        (network, queue)
      }
    })) getOrElse {
      if (locations.contains(location)) {
        (addLocation(network, waypoint, location), queue enqueue location)
      } else {
        (network, queue)
      }
    }
  }

  private def addWaypoint(network: WaypointNetwork,
                          waypoint: Waypoint
                         ): WaypointNetwork = {
    addLocation(network, waypoint, waypoint).copy(
      waypoints = network.waypoints + waypoint
    )
  }

  private def addAdjacent(network: WaypointNetwork,
                          a: Waypoint,
                          b: Waypoint
                         ): WaypointNetwork = {
    val as = network.adjacent.getOrElse(a, Set()) + b
    val bs = network.adjacent.getOrElse(b, Set()) + a

    network.copy(
      adjacent = network.adjacent + (a -> as) + (b -> bs)
    )
  }

  private def addLocation(network: WaypointNetwork,
                          waypoint: Waypoint,
                          location: Location
                         ): WaypointNetwork = {
    val area = network.areas.getOrElse(waypoint, Set()) + location

    network.copy(
      locations = network.locations + (location -> waypoint),
      areas = network.areas + (waypoint -> area)
    )
  }
}

case class WaypointNetwork(waypoints: Set[Waypoint] = Set(),
                           adjacent: Map[Waypoint, Set[Waypoint]] = Map(),
                           locations: Map[Location, Waypoint] = Map(),
                           areas: Map[Waypoint, Set[Location]] = Map()
                          )
