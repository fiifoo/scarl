package io.github.fiifoo.scarl.core.geometry

import io.github.fiifoo.scarl.core.State

import scala.annotation.tailrec
import scala.collection.mutable

/**
  * A* algorithm
  *
  * g = distance traveled from start
  * h = minimum distance to target
  * f = g + h = minimum total cost
  *
  * Start location is excluded.
  * End location is included even if blocked
  */
object Path {

  def apply(s: State)(from: Location, to: Location): Option[Vector[Location]] = {
    calc(Obstacle.has(Obstacle.movement(s)))(from, to)
  }

  def calc(blocked: Location => Boolean)(from: Location, to: Location): Option[Vector[Location]] = {
    if (from == to) {
      return None
    }

    val p = new Process(from, to, Distance.chebyshev, blocked)
    process(p)

    p.result
  }

  case class Node(location: Location, parent: Option[Location], f: Int, g: Int, h: Int)

  case class Constraint(h: Int)

  class Process(val from: Location,
                val to: Location,
                val distance: (Location, Location) => Int,
                val blocked: (Location) => Boolean
               ) {

    val h: Int = distance(from, to)
    val constraint: Constraint = Constraint((h + 1) * 2)
    val start: Node = Node(
      location = from,
      parent = None,
      f = h,
      g = 0,
      h = h
    )

    val queue: Queue = new Queue(to)
    var step: Int = 0
    var closed: Set[Location] = Set()
    var nodes: Map[Location, Node] = Map(from -> start)

    var result: Option[Vector[Location]] = None

    queue.enqueue(start, step)
  }

  class Queue(to: Location) {
    private val queue = mutable.PriorityQueue[(Location, (Int, Int, Int))]()(Ordering.by(_._2))

    def enqueue(node: Node, step: Int): Unit = {
      val order = (
        -node.f, // shorter path
        step, // lifo
        -Distance.manhattan(node.location, to) // visually shorter path
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

      if (!p.closed.contains(location)) {
        processLocation(p, location)
      }
    }
  }

  private def processLocation(p: Process, location: Location): Unit = {
    val node = p.nodes(location)

    p.step = p.step + 1
    p.closed = p.closed + location

    neighbors(location) filterNot (p.closed.contains(_)) foreach (neighbor => {
      processNeighbor(p, node, neighbor)
    })
  }

  private def processNeighbor(p: Process, parent: Node, location: Location): Unit = {
    val g = parent.g + 1
    val h = p.distance(location, p.to)
    val f = g + h

    if (location == p.to) {
      p.result = Some(backtrace(location, parent, p.nodes))
    } else if (p.blocked(location)) {
      p.closed = p.closed + location
    } else if (h > p.constraint.h) {
      // not good enough -> close
      p.closed = p.closed + location
    } else if (!p.nodes.get(location).forall(n => n.g > g)) {
      // better or equal node exists for this location -> skip
    } else {
      val node = Node(
        location,
        Some(parent.location),
        f, g, h
      )

      p.nodes = p.nodes + (location -> node)
      p.queue.enqueue(node, p.step)
    }
  }

  private def neighbors(l: Location): List[Location] = {
    List(
      Location(l.x - 1, l.y - 1),
      Location(l.x - 1, l.y),
      Location(l.x - 1, l.y + 1),
      Location(l.x, l.y + 1),
      Location(l.x + 1, l.y + 1),
      Location(l.x + 1, l.y),
      Location(l.x + 1, l.y - 1),
      Location(l.x, l.y - 1)
    )
  }

  private def backtrace(location: Location, parent: Node, nodes: Map[Location, Node]): Vector[Location] = {
    _backtrace(parent.location, nodes, Vector(location, parent.location)).reverse.tail
  }

  @tailrec
  private def _backtrace(location: Location, nodes: Map[Location, Node], result: Vector[Location]): Vector[Location] = {
    val parentOption = nodes(location).parent

    if (parentOption.isDefined) {
      val parent = parentOption.get
      _backtrace(parent, nodes, result :+ parent)
    } else {
      result
    }
  }
}
