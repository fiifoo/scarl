package io.github.fiifoo.scarl.game.player

import io.github.fiifoo.scarl.action.MoveAction
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.entity.Selectors.{getCreatureKeys, getCreatureStats, getLocationEntities, getLocationItems}
import io.github.fiifoo.scarl.core.entity.{CreatureId, ItemId}
import io.github.fiifoo.scarl.core.geometry.{Location, Obstacle, Path}
import io.github.fiifoo.scarl.game.RunState
import io.github.fiifoo.scarl.game.api.GameUpdate

import scala.annotation.tailrec
import scala.collection.immutable.Queue

object PlayerAutomation {

  def getDestination(state: RunState, direction: Option[Int]): Option[Location] = {
    if (direction.isDefined) {
      getDirectionDestination(state, direction.get)
    } else {
      getExplorationDestination(state)
    }
  }

  def getMoveAction(state: RunState, destination: Location): Option[Action] = {
    if (isTravelBlocked(state)(destination) || getCreatureStats(state.instance)(state.game.player).speed <= 0) {
      return None
    }

    val from = state.game.player(state.instance).location
    val path = Path.calc(isTravelBlocked(state))(from, destination)

    path map (_.head) flatMap (to => {
      if (hasCreature(state)(to) || hasClosedDoor(state)(to)) {
        None
      } else {
        Some(MoveAction(to))
      }
    })
  }

  def shouldContinueMoving(initial: RunState, result: RunState, destination: Location): Boolean = {
    val events = (result.outMessages collect {
      case message: GameUpdate => message.events
    }).flatten

    val player = initial.game.player
    val from = player(initial.instance).location
    val to = player(result.instance).location

    events.isEmpty && from != to && to != destination && !hasNewVisibleEnemies(initial, result)
  }

  private def getExplorationDestination(state: RunState): Option[Location] = {
    val from = state.game.player(state.instance).location
    val keys = getCreatureKeys(state.instance)(state.game.player)
    val blocked = Obstacle.has(Obstacle.travel(state.instance, keys)) _

    @tailrec
    def step(queue: Queue[Location], visited: Set[Location]): Option[Location] = {
      if (queue.isEmpty) {
        return None
      }

      val (location, dequeued) = queue.dequeue
      val adjacent = location.adjacent -- visited
      val allowed = adjacent filterNot blocked

      if (adjacent exists (!state.areaMap.isDefinedAt(_))) {
        Some(location)
      } else {
        step(dequeued.enqueue(allowed), visited ++ adjacent)
      }
    }

    step(Queue(from), Set(from))
  }

  private def getDirectionDestination(state: RunState, direction: Int): Option[Location] = {
    val from = state.game.player(state.instance).location
    val blocked = isTravelBlocked(state)

    @tailrec
    def step(from: Location): Location = {
      val to = getDirectionLocation(from, direction)

      if (blocked(to)) {
        from
      } else {
        step(to)
      }
    }

    val destination = step(from)

    if (destination == from) {
      None
    } else {
      Some(destination)
    }
  }

  private def hasNewVisibleEnemies(initial: RunState, result: RunState): Boolean = {
    val old = initial.fov.locations flatMap getEnemy(initial)
    val current = result.fov.locations flatMap getEnemy(initial)

    (current -- old).nonEmpty
  }

  private def getEnemy(state: RunState)(location: Location): Option[CreatureId] = {
    val enemies = state.game.player(state.instance).faction(state.instance).enemies

    getCreature(state)(location) filter (x => {
      val faction = x(state.instance).faction

      enemies.contains(faction)
    })
  }

  private def getCreature(state: RunState)(location: Location): Option[CreatureId] = {
    getLocationEntities(state.instance)(location) collectFirst {
      case creature: CreatureId if creature != state.game.player => creature
    }
  }

  private def hasCreature(state: RunState)(location: Location): Boolean = {
    getCreature(state)(location).isDefined
  }

  private def hasClosedDoor(state: RunState)(location: Location): Boolean = {
    (getLocationItems(state.instance)(location) collectFirst {
      case item: ItemId if item(state.instance).door.exists(!_.open) => true
    }).isDefined
  }

  private def isTravelBlocked(state: RunState): Location => Boolean = {
    val keys = getCreatureKeys(state.instance)(state.game.player)
    val blocked = Obstacle.has(Obstacle.travel(state.instance, keys)) _

    (location: Location) => !state.areaMap.isDefinedAt(location) || blocked(location)
  }

  private def getDirectionLocation(location: Location, direction: Int): Location = {
    direction match {
      case 1 => Location(location.x - 1, location.y + 1)
      case 2 => Location(location.x, location.y + 1)
      case 3 => Location(location.x + 1, location.y + 1)
      case 4 => Location(location.x - 1, location.y)
      case 6 => Location(location.x + 1, location.y)
      case 7 => Location(location.x - 1, location.y - 1)
      case 8 => Location(location.x, location.y - 1)
      case _ => Location(location.x + 1, location.y - 1)
    }
  }
}
