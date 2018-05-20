package io.github.fiifoo.scarl.area.feature

import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.area.feature.BurrowFeature.{Burrow, CalculateResistance}
import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.area.template.FixedContent
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.world.WorldAssets

import scala.annotation.tailrec
import scala.collection.SortedSet
import scala.collection.immutable.Queue
import scala.util.Random

case class BurrowFeature(min: Int, max: Int, noise: Int) extends Feature {

  def apply(assets: WorldAssets,
            area: Area,
            shape: Shape.Result,
            content: FixedContent,
            locations: Set[Location],
            entrances: Set[Location],
            subEntrances: Set[Location],
            random: Random
           ): FixedContent = {
    val limit = 100 - (min + random.nextInt(this.max - this.min + 1))

    val allLocations = locations ++ entrances
    val walls = content.walls.keys.toSet intersect allLocations

    val resistances = CalculateResistance(allLocations, walls, this.noise, random)
    val burrowed = Burrow(allLocations, walls, resistances, limit, random)

    content.copy(
      walls = content.walls -- burrowed
    )
  }
}

object BurrowFeature {

  object CalculateResistance {

    private val floorResistance = 0
    private val borderResistance = (random: Random) => 85 + random.nextInt(15)

    def apply(locations: Set[Location],
              walls: Set[Location],
              noise: Int,
              random: Random
             ): Map[Location, Int] = {
      val floors = locations -- walls
      val borders = walls filter (wall => getAdjacentLocations(wall) exists (!locations.contains(_)))

      val resistances = floors.map(_ -> floorResistance).toMap ++ borders.map(_ -> borderResistance(random)).toMap
      val processed = floors ++ borders
      val queue = processed flatMap getAdjacentLocations filter (adjacent => {
        !processed.contains(adjacent) && locations.contains(adjacent)
      })
      val visited = processed ++ queue

      step(resistances, visited, Queue().enqueue(queue), noise, random)
    }

    @tailrec
    private def step(resistances: Map[Location, Int],
                     visited: Set[Location],
                     queue: Queue[Location],
                     noise: Int,
                     random: Random
                    ): Map[Location, Int] = {
      if (queue.isEmpty) {
        return resistances
      }

      val (location, dequeued) = queue.dequeue
      val adjacent = getAdjacentLocations(location)
      val resistance = calculateResistance(adjacent.toList flatMap resistances.get, noise, random)

      step(
        resistances + (location -> resistance),
        visited + location ++ adjacent,
        dequeued.enqueue(adjacent diff visited),
        noise,
        random
      )
    }

    private def calculateResistance(adjacent: List[Int], noise: Int, random: Random): Int = {
      if (adjacent.isEmpty) {
        return 0
      }

      val base = adjacent.sum / adjacent.size

      val _noise = math.max(noise, 1)
      val min = math.max(base - _noise, 0)
      val max = math.min(base + _noise, 100)

      min + random.nextInt(max - min)
    }
  }

  object Burrow {
    private implicit val ordering = Ordering.by[(Location, Int), (Int, Int)](v => {
      (v._2, v._1.hashCode)
    })

    def apply(locations: Set[Location],
              walls: Set[Location],
              resistances: Map[Location, Int],
              limit: Int,
              random: Random
             ): Set[Location] = {
      @tailrec
      def step(walls: Set[Location], targets: SortedSet[(Location, Int)]): Set[Location] = {
        if (targets.isEmpty || walls.size * 100 / locations.size <= limit) {
          walls
        } else {
          val target = targets.head
          val location = target._1

          val adjacent = getAdjacentLocations(location) intersect walls flatMap (adjacent => {
            resistances.get(adjacent) map (adjacent -> _)
          })

          step(walls - location, targets - target ++ adjacent)
        }
      }

      val remaining = step(walls, getTargets(locations, walls, resistances, random))

      walls -- remaining
    }

    private def getTargets(locations: Set[Location],
                           walls: Set[Location],
                           resistances: Map[Location, Int],
                           random: Random
                          ): SortedSet[(Location, Int)] = {
      val floors = locations -- walls

      val targets = if (floors.isEmpty) {
        Set(Rng.nextChoice(random, walls))
      } else {
        walls filter (wall => getAdjacentLocations(wall) exists floors.contains)
      }

      SortedSet() ++ (resistances filterKeys targets.contains)
    }
  }

  private def getAdjacentLocations(l: Location): Set[Location] = {
    Set(
      Location(l.x, l.y + 1),
      Location(l.x, l.y - 1),
      Location(l.x + 1, l.y),
      Location(l.x - 1, l.y)
    )
  }
}
