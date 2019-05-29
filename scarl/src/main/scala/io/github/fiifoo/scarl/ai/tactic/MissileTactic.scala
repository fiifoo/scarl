package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.action.{ExplodeAction, MoveAction, PassAction}
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.ai.{Behavior, Intention, Priority}
import io.github.fiifoo.scarl.core.creature.Missile
import io.github.fiifoo.scarl.core.creature.Missile.Smart
import io.github.fiifoo.scarl.core.entity.Selectors.getCreatureStats
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.geometry.{Line, Location, Obstacle, Path}

import scala.util.Random

case class MissileTactic(destination: Location, target: Option[SafeCreatureId]) extends Behavior {

  val intentions: List[(Intention, Priority.Value)] = List()

  def behavior(s: State, actor: CreatureId, random: Random): Result = {
    apply(s, actor, random) getOrElse(this, PassAction)
  }

  override def apply(s: State, actor: CreatureId, random: Random): Option[Result] = {
    actor(s).missile flatMap (missile => {
      val from = actor(s).location
      val flight = actor(s).flying

      if (from == destination) {
        Some(explode)
      } else {
        val nextDestination = getNextDestination(s, missile)
        val path = getPath(s, flight, missile, from, nextDestination)

        path map (path => {
          val to = path.head
          if (Obstacle.movement(s, flight)(to).isDefined) {
            explode
          } else if (getCreatureStats(s)(actor).speed > 0) {
            move(to, nextDestination)
          } else {
            pass(nextDestination)
          }
        })
      }
    })
  }

  private def explode: Result = {
    (this, ExplodeAction)
  }

  private def move(to: Location, nextDestination: Location): Result = {
    val tactic = this.copy(destination = nextDestination)
    val action = MoveAction(to)

    (tactic, action)
  }

  private def pass(nextDestination: Location): Result = {
    val tactic = this.copy(destination = nextDestination)
    val action = PassAction

    (tactic, action)
  }

  private def getNextDestination(s: State, missile: Missile): Location = {
    if (missile.guidance.isDefined) {
      getTargetLocation(s) getOrElse destination
    } else {
      destination
    }
  }

  private def getPath(s: State, flight: Boolean, missile: Missile, from: Location, to: Location): Option[Vector[Location]] = {
    missile.guidance collect {
      case Smart => Path(s, flight)(from, to)
    } getOrElse {
      Some(Line(from, to).tail)
    }
  }

  private def getTargetLocation(s: State): Option[Location] = {
    target flatMap (_ (s) map (_.location))
  }
}
