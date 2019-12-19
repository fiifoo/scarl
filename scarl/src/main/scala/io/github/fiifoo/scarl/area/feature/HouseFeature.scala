package io.github.fiifoo.scarl.area.feature

import io.github.fiifoo.scarl.area.feature.HouseFeature.{Build, Scan}
import io.github.fiifoo.scarl.area.shape.Shape
import io.github.fiifoo.scarl.area.template.ContentSelection._
import io.github.fiifoo.scarl.area.template.FixedContent
import io.github.fiifoo.scarl.area.template.Template.Context
import io.github.fiifoo.scarl.core.geometry.{Location, Rotation}
import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.world.WorldAssets

import scala.util.Random

case class HouseFeature(iterations: Int,
                        roomSize: Int,
                        doorFactor: Int,
                        wall: Option[WallSelection] = None,
                        door: Option[DoorSelection] = None
                       ) extends Feature {

  def apply(assets: WorldAssets,
            context: Context,
            shape: Shape.Result,
            content: FixedContent,
            locations: Set[Location],
            entrances: Set[Location],
            subEntrances: Set[Location],
            random: Random
           ): FixedContent = {
    val rotation = Rotation(random, shape.outerWidth, shape.outerHeight)

    val reserved = entrances ++
      subEntrances ++
      content.restrictedLocations ++
      content.conduitLocations.keys ++
      content.creatures.keys ++
      content.gatewayLocations ++
      content.items.keys ++
      content.widgets.keys

    val wall = this.wall getOrElse ThemeWall()
    val door = this.door getOrElse ThemeDoor()

    val (result, _) = ((0 until this.iterations) foldLeft(content, rotation)) ((carry, _) => {
      val (content, rotation) = carry
      val (width, height) = rotation.shape

      def hasWall(location: Location): Boolean = {
        val normalized = rotation(location)

        (!locations.contains(normalized) && !entrances.contains(normalized) && !subEntrances.contains(normalized)) ||
          content.walls.isDefinedAt(normalized)
      }

      def hasReserved(location: Location): Boolean = {
        reserved.contains(rotation(location))
      }

      val foundations = Scan(width, height, hasWall, hasReserved)
      val result = Build(foundations, roomSize, doorFactor, random)

      (write(content, rotation, result, wall, door), rotation.next)
    })

    result
  }

  private def write(content: FixedContent,
                    rotation: Rotation,
                    result: Build.Result,
                    wall: WallSelection,
                    door: DoorSelection
                   ): FixedContent = {
    val walls = (result.walls map rotation.apply map (_ -> wall)).toMap
    val items = (result.doors map rotation.apply map (location => {
      val items = content.items.getOrElse(location, List())

      location -> (door :: items)
    })).toMap

    content.copy(
      walls = content.walls ++ walls,
      items = content.items ++ items
    )
  }
}

object HouseFeature {
  type X = Int
  type Y = Int

  case class Line(y: Y, left: X, right: X) {
    def size: Int = right - left + 1

    def intersect(line: Line): Option[Line] = {
      val left = math.max(this.left, line.left)
      val right = math.min(this.right, line.right)

      if (left > right) {
        None
      } else {
        Some(this.copy(left = left, right = right))
      }
    }

    def complement(lines: List[Line]): List[Line] = {
      lines flatMap (line => {
        val left = if (line.left < this.left) {
          Some(line.copy(right = math.min(line.right, this.left - 1)))
        } else {
          None
        }

        val right = if (line.right > this.right) {
          Some(line.copy(left = math.max(line.left, this.right + 1)))
        } else {
          None
        }

        List(left, right).flatten
      })
    }
  }

  type FoundationId = Int

  object Foundation {
    def apply(id: FoundationId, from: Line): Foundation = {
      Foundation(id, from, List(from))
    }
  }

  case class Foundation(id: FoundationId,
                        from: Line,
                        remaining: List[Line],
                        floors: List[Line] = List(),
                        to: List[Line] = List()
                       )

  object Scan {
    def apply(width: Int, height: Int, wall: HasWall, reserved: HasReserved): Iterable[Foundation] = {
      val result = (0 until height foldLeft Foundations()) (scanRow(width, wall, reserved))

      result.complete.values
    }

    type HasWall = Location => Boolean
    type HasReserved = Location => Boolean

    case class Foundations(current: Map[FoundationId, Foundation] = Map(),
                           complete: Map[FoundationId, Foundation] = Map(),
                           nextId: Int = 1,
                          )

    trait Buffer {
      def flush(foundations: Foundations): Foundations
    }

    case class FromBuffer(line: Line) extends Buffer {
      def apply(right: X): FromBuffer = copy(line = line.copy(right = right))

      def flush(foundations: Foundations): Foundations = {
        if (line.size < 3) {
          return foundations
        }

        val id = foundations.nextId
        val foundation = Foundation(id, this.line)

        foundations.copy(
          current = foundations.current + (id -> foundation),
          nextId = id + 1
        )
      }
    }

    case class FloorBuffer(line: Line) extends Buffer {
      def apply(right: X): FloorBuffer = copy(line = this.line.copy(right = right))

      def flush(foundations: Foundations): Foundations = {
        val intersecting = foundations.current
          .filter(_._2.from.intersect(this.line).nonEmpty)
          .transform((_, f) => f.copy(floors = this.line :: f.floors))

        foundations.copy(current = foundations.current ++ intersecting)
      }
    }

    case class ReservedBuffer(line: Line) extends Buffer {
      def apply(right: X): ReservedBuffer = copy(line = this.line.copy(right = right))

      def flush(foundations: Foundations): Foundations = {
        flushTerminalBuffer(this.line, foundations, writeTo = false)
      }
    }

    case class ToBuffer(line: Line) extends Buffer {
      def apply(right: X): ToBuffer = copy(line = this.line.copy(right = right))

      def flush(foundations: Foundations): Foundations = {
        flushTerminalBuffer(this.line, foundations, writeTo = true)
      }
    }

    private def flushTerminalBuffer(line: Line, foundations: Foundations, writeTo: Boolean): Foundations = {
      def intersect[K](k: K, foundation: Foundation): Foundation = {
        val to = foundation.remaining flatMap line.intersect

        if (to.isEmpty) {
          return foundation
        }

        val remaining = (to foldLeft foundation.remaining) ((remaining, to) => to.complement(remaining))

        foundation.copy(
          to = if (writeTo) foundation.to ::: to else foundation.to,
          remaining = remaining
        )
      }

      val intersecting = foundations.current transform intersect
      val (complete, current) = intersecting partition (_._2.remaining.isEmpty)

      foundations.copy(
        current = foundations.current ++ current -- complete.keys,
        complete = foundations.complete ++ complete
      )
    }

    case class Buffers(from: Option[FromBuffer] = None,
                       floor: Option[FloorBuffer] = None,
                       reserved: Option[ReservedBuffer] = None,
                       to: Option[ToBuffer] = None
                      ) {
      def flush(foundations: Foundations): Foundations = {
        (List(from, floor, reserved, to).flatten foldLeft foundations) ((foundations, buffer) => buffer.flush(foundations))
      }
    }

    private val scanners = List(scanFrom _, scanFloor _, scanReserved _, scanTo _)

    private def scanRow(width: Int, wall: HasWall, reserved: HasReserved)
                       (foundations: Foundations, y: Y): Foundations = {
      val (result, buffers) = (0 until width foldLeft(foundations, Buffers())) (scanLocation(y, wall, reserved))

      buffers.flush(result)
    }

    private def scanLocation(y: Y, wall: HasWall, reserved: HasReserved)
                            (carry: (Foundations, Buffers), x: X): (Foundations, Buffers) = {
      val location = Location(x, y)

      (scanners foldLeft carry) ((carry, scanner) => {
        val (foundations, buffers) = carry

        scanner(foundations, buffers, location, wall, reserved)
      })
    }

    private def scanFrom(foundations: Foundations,
                         buffers: Buffers,
                         location: Location,
                         wall: HasWall,
                         reserved: HasReserved
                        ): (Foundations, Buffers) = {
      if (!wall(location) && wall(above(location))) {
        val buffer = buffers.from map (buffer => {
          buffer(location.x)
        }) getOrElse {
          FromBuffer(Line(location.y, location.x, location.x))
        }

        (foundations, buffers.copy(from = Some(buffer)))
      } else {
        buffers.from map (buffer => {
          (buffer.flush(foundations), buffers.copy(from = None))
        }) getOrElse {
          (foundations, buffers)
        }
      }
    }

    private def scanFloor(foundations: Foundations,
                          buffers: Buffers,
                          location: Location,
                          wall: HasWall,
                          reserved: HasReserved
                         ): (Foundations, Buffers) = {
      if (!wall(location)) {
        val buffer = buffers.floor map (buffer => {
          buffer(location.x)
        }) getOrElse {
          FloorBuffer(Line(location.y, location.x, location.x))
        }

        (foundations, buffers.copy(floor = Some(buffer)))
      } else {
        buffers.floor map (buffer => {
          (buffer.flush(foundations), buffers.copy(floor = None))
        }) getOrElse {
          (foundations, buffers)
        }
      }
    }

    private def scanReserved(foundations: Foundations,
                             buffers: Buffers,
                             location: Location,
                             wall: HasWall,
                             reserved: HasReserved
                            ): (Foundations, Buffers) = {
      if (!wall(location) && reserved(location)) {
        val buffer = buffers.reserved map (buffer => {
          buffer(location.x)
        }) getOrElse {
          ReservedBuffer(Line(location.y, location.x, location.x))
        }

        (foundations, buffers.copy(reserved = Some(buffer)))
      } else {
        buffers.reserved map (buffer => {
          (buffer.flush(foundations), buffers.copy(reserved = None))
        }) getOrElse {
          (foundations, buffers)
        }
      }
    }

    private def scanTo(foundations: Foundations,
                       buffers: Buffers,
                       location: Location,
                       wall: HasWall,
                       reserved: HasReserved
                      ): (Foundations, Buffers) = {
      if (wall(location) && !wall(above(location))) {
        val buffer = buffers.to map (buffer => {
          buffer(location.x)
        }) getOrElse {
          ToBuffer(Line(location.y, location.x, location.x))
        }

        (foundations, buffers.copy(to = Some(buffer)))
      } else {
        buffers.to map (buffer => {
          (buffer.flush(foundations), buffers.copy(to = None))
        }) getOrElse {
          (foundations, buffers)
        }
      }
    }

    private def above(location: Location): Location = Location(location.x, location.y - 1)
  }

  object Build {

    case class Result(walls: Set[Location] = Set(), doors: Set[Location] = Set())

    def apply(foundations: Iterable[Foundation], roomSize: Int, doorFactor: Int, random: Random): Result = {
      (foundations foldLeft Result()) (build(roomSize, doorFactor, random))
    }

    private def build(roomSize: Int, doorFactor: Int, random: Random)
                     (result: Result, foundation: Foundation): Result = {
      val from = foundation.from
      val to = foundation.to
        .flatMap(selectTo(roomSize, foundation))
        .sortBy(to => (to.y, to.size))
        .headOption

      to map (to => {
        val x = to.left + random.nextInt(to.size)

        val walls = (from.y until to.y) map (y => Location(x, y))
        val doors = selectDoors(walls, doorFactor, random)

        result.copy(
          walls = result.walls ++ walls -- doors,
          doors = result.doors ++ doors
        )
      }) getOrElse {
        result
      }
    }

    private def selectDoors(walls: Iterable[Location], doorFactor: Int, random: Random): Set[Location] = {
      def select(walls: Iterable[Location], result: Set[Location]): Set[Location] = {
        val validWalls = if (walls.size > 1) walls.init else walls
        val (choices, rest) = validWalls.splitAt(doorFactor)
        val door = Rng.nextChoice(random, choices)

        if (rest.isEmpty) {
          result + door
        } else {
          select(rest, result + door)
        }
      }

      select(walls, Set())
    }

    private def selectTo(roomSize: Int, foundation: Foundation)(to: Line): Option[Line] = {
      if (math.abs(to.y - foundation.from.y) < roomSize) {
        return None
      }

      val (left, right) = constrictTo(roomSize, foundation, to)

      if (left > right) {
        None
      } else {
        Some(to.copy(left = left, right = right))
      }
    }

    private def constrictTo(roomSize: Int, foundation: Foundation, to: Line): (X, X) = {
      val floors = foundation.floors
        .filter(floor => floor.y < to.y && floor.intersect(to).isDefined)
        .drop(1)

      if (floors.isEmpty) {
        return (to.left + 1, to.right - 1)
      }

      val head = floors.head
      val initial = (head.left, head.left, head.right, head.right)

      val (maxLeft, minLeft, maxRight, minRight) = (floors.tail foldLeft initial) ((carry, floor) => {
        val (maxLeft, minLeft, maxRight, minRight) = carry

        (
          math.max(maxLeft, floor.left),
          math.min(minLeft, floor.left),
          math.max(maxRight, floor.right),
          math.min(minRight, floor.right),
        )
      })

      val left = math.max(to.left, math.max(maxLeft, minLeft + roomSize - 1) + 1)
      val right = math.min(to.right, math.min(minRight, maxRight - roomSize + 1) - 1)

      (left, right)
    }
  }

}
