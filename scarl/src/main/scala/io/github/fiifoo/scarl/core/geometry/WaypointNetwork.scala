package io.github.fiifoo.scarl.core.geometry

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.geometry.WaypointNetwork.Waypoint

import scala.annotation.tailrec
import scala.collection.immutable.Queue

object WaypointNetwork {

  type Waypoint = Location

  def apply(s: State): WaypointNetwork = {
    val sectors = Sector.sectors(s)
    val calculate = calculateSector(s, blocked(s)) _

    sectors.foldLeft(WaypointNetwork())(calculate)
  }

  def recalculate(s: State, network: WaypointNetwork, locations: Set[Location]): WaypointNetwork = {
    val sectors = locations map Sector(s)

    (sectors foldLeft network) ((network, sector) => {
      val cleared = clearSector(s, network, sector)

      calculateSector(s, blocked(s))(cleared, sector)
    })
  }

  private def blocked(s: State): Location => Boolean = {
    Obstacle.has(Obstacle.travel(s))
  }

  private def calculateSector(s: State,
                              blocked: Location => Boolean)
                             (network: WaypointNetwork,
                              sector: Sector
                             ): WaypointNetwork = {
    val locations = sector.locations(s) filterNot blocked

    calculateWaypoint(s, network, sector, locations)
  }

  private def calculateWaypoint(s: State,
                                network: WaypointNetwork,
                                sector: Sector,
                                locations: Set[Location]
                               ): WaypointNetwork = {
    sector.center(s).closest(locations) map (waypoint => {
      val nextNetwork = calculateWaypointLocations(
        network = addWaypoint(network, waypoint),
        waypoint = waypoint,
        queue = Queue(waypoint),
        locations = locations
      )
      val nextLocations = locations -- nextNetwork.locationWaypoint.keys

      if (nextLocations.nonEmpty) {
        calculateWaypoint(s, nextNetwork, sector, nextLocations)
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

    (network.locationWaypoint.get(location) map (existing => {
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
    val as = network.adjacentWaypoints.getOrElse(a, Set()) + b
    val bs = network.adjacentWaypoints.getOrElse(b, Set()) + a

    network.copy(
      adjacentWaypoints = network.adjacentWaypoints + (a -> as) + (b -> bs)
    )
  }

  private def addLocation(network: WaypointNetwork,
                          waypoint: Waypoint,
                          location: Location
                         ): WaypointNetwork = {
    val area = network.waypointLocations.getOrElse(waypoint, Set()) + location

    network.copy(
      locationWaypoint = network.locationWaypoint + (location -> waypoint),
      waypointLocations = network.waypointLocations + (waypoint -> area)
    )
  }

  private def clearSector(s: State, network: WaypointNetwork, sector: Sector): WaypointNetwork = {
    val locations = sector.locations(s)
    val waypoints = network.waypoints intersect locations

    network.copy(
      waypoints = network.waypoints -- waypoints,
      adjacentWaypoints = clearAdjacentWaypoints(network, waypoints),
      locationWaypoint = network.locationWaypoint -- locations,
      waypointLocations = network.waypointLocations -- waypoints
    )
  }

  private def clearAdjacentWaypoints(network: WaypointNetwork, waypoints: Set[Waypoint]): Map[Waypoint, Set[Waypoint]] = {
    val cleared = (waypoints foldLeft network.adjacentWaypoints) ((adjacentWaypoints, waypoint) => {
      adjacentWaypoints.get(waypoint) map (adjacent => {
        (adjacent foldLeft adjacentWaypoints) ((adjacentWaypoints, adjacentWaypoint) => {
          val next = adjacentWaypoints(adjacentWaypoint) - waypoint

          if (next.isEmpty) {
            adjacentWaypoints - adjacentWaypoint
          } else {
            adjacentWaypoints + (adjacentWaypoint -> next)
          }
        })
      }) getOrElse adjacentWaypoints
    })

    cleared -- waypoints
  }
}

case class WaypointNetwork(waypoints: Set[Waypoint] = Set(),
                           adjacentWaypoints: Map[Waypoint, Set[Waypoint]] = Map(),
                           locationWaypoint: Map[Location, Waypoint] = Map(),
                           waypointLocations: Map[Waypoint, Set[Location]] = Map()
                          )
