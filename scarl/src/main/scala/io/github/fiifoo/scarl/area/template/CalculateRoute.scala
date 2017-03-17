package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.geometry.Distance

import scala.annotation.tailrec
import scala.collection.mutable

object CalculateRoute {

  def apply(from: Location, to: Location, locations: Set[Location]): Option[Set[Location]] = {
    val p = new Process(from, to, locations, Distance.manhattan)
    process(p)

    p.result map (_ - from - to)
  }

  case class Node(location: Location, parent: Option[Location], distance: Int)

  class Process(val from: Location,
                val to: Location,
                val locations: Set[Location],
                val distance: (Location, Location) => Int
               ) {

    val start: Node = Node(
      location = from,
      parent = None,
      distance = distance(from, to)
    )

    val queue: Queue = new Queue(to)
    var step: Int = 0
    var nodes: Map[Location, Node] = Map(from -> start)

    var result: Option[Set[Location]] = None

    queue.enqueue(start, step)
  }

  class Queue(to: Location) {
    private val queue = mutable.PriorityQueue[(Location, (Int, Int))]()(Ordering.by(_._2))

    def enqueue(node: Node, step: Int): Unit = {
      val order = (
        -node.distance, // closer to destination
        step // lifo
      )

      queue.enqueue((node.location, order))
    }

    def dequeue(): Location = {
      val (location, _) = queue.dequeue()

      location
    }

    def nonEmpty: Boolean = queue.nonEmpty
  }

  private def process(p: Process): Unit = {
    while (p.result.isEmpty && p.queue.nonEmpty) {
      val location = p.queue.dequeue()

      processLocation(p, location)
    }
  }

  private def processLocation(p: Process, location: Location): Unit = {
    val node = p.nodes(location)

    p.step = p.step + 1

    neighbors(location) filterNot p.nodes.isDefinedAt foreach (neighbor => {
      processNeighbor(p, node, neighbor)
    })
  }

  private def processNeighbor(p: Process, parent: Node, location: Location): Unit = {
    if (location == p.to) {
      p.result = Some(backtrace(location, parent, p.nodes))
    } else if (!p.locations.contains(location)) {
      // not available -> skip
    } else {
      val node = Node(
        location,
        Some(parent.location),
        p.distance(location, p.to)
      )

      p.nodes = p.nodes + (location -> node)
      p.queue.enqueue(node, p.step)
    }
  }

  private def neighbors(l: Location): List[Location] = {
    List(
      Location(l.x - 1, l.y),
      Location(l.x, l.y + 1),
      Location(l.x + 1, l.y),
      Location(l.x, l.y - 1)
    )
  }

  private def backtrace(location: Location, parent: Node, nodes: Map[Location, Node]): Set[Location] = {
    _backtrace(parent.location, nodes, Set(location, parent.location))
  }

  @tailrec
  private def _backtrace(location: Location, nodes: Map[Location, Node], result: Set[Location]): Set[Location] = {
    val parentOption = nodes(location).parent

    if (parentOption.isDefined) {
      val parent = parentOption.get
      _backtrace(parent, nodes, result + parent)
    } else {
      result
    }
  }


}
